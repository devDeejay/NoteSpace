package xyz.devdeejay.notespace.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import xyz.devdeejay.notespace.database.NoteEntity;

public class SampleData {
    private static final String SAMPLE_TEXT_1 = "A simple data";
    private static final String SAMPLE_TEXT_2 = "A note with a\nline feed";
    private static final String SAMPLE_TEXT_3 = "A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data A simple data ";

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<NoteEntity> getNotes() { // Sample Data to for testing purposes
        List<NoteEntity> notes = new ArrayList<>();
        notes.add(new NoteEntity(1, getDate(-1), SAMPLE_TEXT_1));
        notes.add(new NoteEntity(2, getDate(-2), SAMPLE_TEXT_2));
        notes.add(new NoteEntity(3, getDate(-3), SAMPLE_TEXT_3));
        return notes;
    }
}
