/*created by sevagh*/

#include <float.h>
#include <limits.h>
#include <jni.h>

#define BUFFER_SIZE 1024
#define DEFAULT_CUTOFF 0.93 //0.97 is default
#define SMALL_CUTOFF 0.5
#define LOWER_PITCH_CUTOFF 80 //hz
#define SAMPLE_RATE 48000

#define MAX(a,b) ((a < b) ?  (b) : (a))

double turning_point_x, turning_point_y;

int max_positions[BUFFER_SIZE];

double nsdf[BUFFER_SIZE];
double period_estimates[BUFFER_SIZE];
double amp_estimates[BUFFER_SIZE];

int max_positions_ptr, period_estimates_ptr, amp_estimates_ptr;

static double get_pitch(double *data);
static void normalized_square_difference(double *audio_buffer);
double get_pitch_from_short(short *data);

JNIEXPORT jdouble JNICALL Java_com_sevag_pitcha_recording_AudioRecorder_get_1pitch_1from_1short
  (JNIEnv *env, jclass cls, jshortArray arr)
{
    return (jdouble) get_pitch_from_short((short *) arr);
}

double get_pitch_from_short(short *data)
{
	double double_data[BUFFER_SIZE];

	short maxshort = 0;
	int i;
	for (i = 0; i < BUFFER_SIZE; i++) {
		if (data[i] > maxshort) {
			maxshort = data[i];
		}
	}

	for (i = 0; i < BUFFER_SIZE; i++) {
		double_data[i] = (double) ((double) data[i] * (double) INT_MAX
					   /(double) maxshort);
	}

	return get_pitch(double_data);
}

static void normalized_square_difference(double *audio_buffer)
{
	int tau;
	for (tau = 0; tau < BUFFER_SIZE; tau++) {
		double acf = 0;
		double divisorM = 0;
		int i;
		for (i = 0; i < BUFFER_SIZE - tau; i++) {
			acf += audio_buffer[i] * audio_buffer[i + tau];
			divisorM += audio_buffer[i] * audio_buffer[i] + audio_buffer[i + tau] * audio_buffer[i + tau];
		}
		nsdf[tau] = 2 * acf / divisorM;
	}
}

static void parabolic_interpolation(int tau)
{
	double nsdfa = nsdf[tau - 1];
	double nsdfb = nsdf[tau];
	double nsdfc = nsdf[tau + 1];
	double bValue = tau;
	double bottom = nsdfc + nsdfa - 2 * nsdfb;
	if (bottom == 0.0) {
		turning_point_x = bValue;
		turning_point_y = nsdfb;
	} else {
		double delta = nsdfa - nsdfc;
		turning_point_x = bValue + delta / (2 * bottom);
		turning_point_y = nsdfb - delta * delta / (8 * bottom);
	}
}

static void peak_picking()
{
	int pos = 0;
	int curMaxPos = 0;

	// find the first negative zero crossing
	while (pos < (BUFFER_SIZE - 1) / 3 && nsdf[pos] > 0) {
		pos++;
	}

	// loop over all the values below zero
	while (pos < BUFFER_SIZE - 1 && nsdf[pos] <= 0.0) {
		pos++;
	}

	// can happen if output[0] is NAN
	if (pos == 0) {
		pos = 1;
	}

	while (pos < BUFFER_SIZE - 1) {
		if (nsdf[pos] > nsdf[pos - 1] && nsdf[pos] >= nsdf[pos + 1]) {
			if (curMaxPos == 0) {
				// the first max (between zero crossings)
				curMaxPos = pos;
			} else if (nsdf[pos] > nsdf[curMaxPos]) {
				// a higher max (between the zero crossings)
				curMaxPos = pos;
			}
		}
		pos++;
		// a negative zero crossing
		if (pos < BUFFER_SIZE - 1 && nsdf[pos] <= 0) {
			// if there was a maximum add it to the list of maxima
			if (curMaxPos > 0) {
				max_positions[max_positions_ptr++] = curMaxPos;
				curMaxPos = 0; // clear the maximum position, so we start
				// looking for a new ones
			}
			while (pos < BUFFER_SIZE - 1 && nsdf[pos] <= 0.0f) {
				pos++; // loop over all the values below zero
			}
		}
	}
	if (curMaxPos > 0) { // if there was a maximum in the last part
		max_positions[max_positions_ptr++] = curMaxPos; // add it to the vector of maxima
	}
}

static double get_pitch(double *audio_buffer)
{
	double pitch;

	max_positions_ptr = 0;
	period_estimates_ptr = 0;
	amp_estimates_ptr = 0;

	normalized_square_difference(audio_buffer);
	peak_picking();

	double highestAmplitude = - DBL_MAX;

	int i, tau;
	for (i = 0; i < max_positions_ptr; i++) {
		tau = max_positions[i];
		highestAmplitude = MAX(highestAmplitude, nsdf[tau]);

		if (nsdf[tau] > SMALL_CUTOFF) {
			parabolic_interpolation(tau);
			amp_estimates[amp_estimates_ptr++] = turning_point_y;
			period_estimates[period_estimates_ptr++] = turning_point_x;
			highestAmplitude = MAX(highestAmplitude, turning_point_y);
		}
	}

	if (period_estimates_ptr == 0) {
		pitch = -1;
	} else {
		double actualCutoff = DEFAULT_CUTOFF * highestAmplitude;

		int periodIndex = 0;
		for (i = 0; i < amp_estimates_ptr; i++) {
			if (amp_estimates[i] >= actualCutoff) {
				periodIndex = i;
				break;
			}
		}

		double period = period_estimates[periodIndex];
		double pitchEstimate = (double) (SAMPLE_RATE / period);
		if (pitchEstimate > LOWER_PITCH_CUTOFF) {
			pitch = pitchEstimate;
		} else {
			pitch = -1;
		}
	}
	return pitch;
}
