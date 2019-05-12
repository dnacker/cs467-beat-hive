package com.example.metrognome.time;

import android.support.annotation.NonNull;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a measure of time.
 */
public class Measure implements Iterable<Beat> {
    public static final int MAX_BPM = 400;
    public static final int MIN_BPM = 1;

    private static final long MILLIS_PER_BPM = 60000;
    private static final int DEFAULT_TEMPO = 60;

    private Rhythm rhythm;
    private int index;
    private TimeSignature timeSignature;
    private int tempo;
    private List<Beat> beats = new ArrayList<>();

    /**
     * Creates a simple measure of 4/4 time at 60 bpm.
     */
    public Measure(Rhythm rhythm, int index) {
        this(rhythm, index, TimeSignature.COMMON_TIME, DEFAULT_TEMPO);
    }

    /**
     * Create a measure with the given {@link TimeSignature} and tempo in BPM.
     * @param rhythm the rhythm that this measure belongs to
     * @param index the index of this measure
     * @param timeSignature the time signature (i.e. 4/4)
     * @param tempo the tempo in BPM.
     */
    public Measure(Rhythm rhythm, int index, TimeSignature timeSignature, int tempo) {
        this.rhythm = rhythm;
        this.index = index;
        setTimeSignature(timeSignature);
        setTempo(tempo);
    }

    /**
     * Sets the time signature for the measure and gives default beats.
     * @param timeSignature the time signature.
     */
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
        for (int i = 0; i < timeSignature.getBeats(); i++) {
            beats.add(new Beat(this, i));
        }
    }

    /**
     * Sets the tempo for this measure and updates the offsets for each beat.
     * @param tempo the tempo in bpm.
     */
    public void setTempo(int tempo) {
        if (tempo < MIN_BPM || tempo >= MAX_BPM) {
            throw new IllegalArgumentException(String.format("Tempo must be between %d and %d: %d", MIN_BPM, MAX_BPM, tempo));
        }
        this.tempo = tempo;
    }

    public long getBeatOffsetMillisAt(int index) {
        return index * getMillisPerBeat();
    }

    public long getSubdivisionOffsetMillisAt(int index, int subdivision) {
        return getBeatOffsetMillisAt(index) + (subdivision * getMillisPerBeat()) / getBeatAt(index).getSubdivisions();
    }


    public int getBeatCount() {
        return beats.size();
    }

    public long getTotalMillis() {
        return getBeatCount() * getMillisPerBeat();
    }

    private long getMillisPerBeat() {
        return MILLIS_PER_BPM / tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public Beat getBeatAt(int index) {
        return beats.get(index);
    }

    public void subdivideBeatAt(int index, int subdivisions) {
        getBeatAt(index).subdivideBy(subdivisions);
    }

    public void playBeatAtSubdivisionAt(int index, int subdivision, SoundPoolWrapper soundPool) {
        getBeatAt(index).playSubdivisionAt(subdivision, soundPool);
    }

    public Rhythm getRhythm() {
        return rhythm;
    }

    public int getIndex() {
        return index;
    }

    @NonNull
    @Override
    public Iterator<Beat> iterator() {
        return beats.iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Measure: \n");
        stringBuilder.append("TimeSignature: " + timeSignature + "\n");
        for (int i = 0; i < beats.size(); i++) {
            stringBuilder.append(beats.get(i));
            if (i != beats.size() - 1) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}