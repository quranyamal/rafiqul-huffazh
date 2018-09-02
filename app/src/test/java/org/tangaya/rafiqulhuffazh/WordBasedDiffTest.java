package org.tangaya.rafiqulhuffazh;

import org.junit.Test;
import org.tangaya.rafiqulhuffazh.util.diff.AdvancedWordBasedDiff;
import org.tangaya.rafiqulhuffazh.util.diff.diff_match_patch;

import java.util.LinkedList;

public class WordBasedDiffTest {

    @Test
    public void wordBaseDiffTest_SameText() {
        String source = "This is a test of word diff";
        String target = "These are tests of diffs";
        LinkedList<diff_match_patch.Diff> linkedList = AdvancedWordBasedDiff.getWordBasedDiff(source, target);

        System.out.println(linkedList);
    }

}
