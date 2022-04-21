package com.cgi.codinggame.dico;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TestDictionnaire {

    @Test
    public void testDictionnaire(){
        String dicoA="rythme,barre,veste,arbre,robe,barbe";
        List<String> dico = Arrays.asList(dicoA.split(","));

        String word1="algorithme";
        String word2="bare";
        String word3="vetse";
        Assert.assertEquals("rythme", Dictionnaire.closest(word1,dico));
        Assert.assertEquals("barbe", Dictionnaire.closest(word2,dico));
        Assert.assertEquals("veste1", Dictionnaire.closest(word3,dico));
        System.out.println("ok");

    }
}
