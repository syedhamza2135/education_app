package com.hamza.app.dictionary;

import java.util.HashSet;
import java.util.Set;

public class SharedClass {

    public static String mainOption, tense_id, storyTitle, storyDesc = null;
    public static String conversationtitle = null;
    public static String conversationId = null;
    public static String conversationdialogtitle = null;
    public static String conversationType_id = null;
    public static String alphabet = null;
    public static int total, attempted, right, wrong = 0;
    public static int question_position = 1;
    public static Set<String> words = new HashSet<String>();
    public static Set<String> meanings = new HashSet<String>();

}
