package com.cgi.codinggame.dico;

import java.util.*;

public class Dictionnaire
{

        public static void main(String[] argv) throws Exception {
            String dicoA="rythme,barre,veste,arbre,robe,barbe";
            List<String> dico = Arrays.asList(dicoA.split(","));
            String wordsA="algorithme,bare,vetse";
            List<String> words = Arrays.asList(wordsA.split(","));

            //Trouver le mot le plus proche
            for (String word : words){
                System.out.println(closest(word, dico));
            }
        }

        public static String closest(String word, List<String> dico) {
            List<Integer> poids = getPoids(word, dico);

            Integer minPoids = Collections.min(poids);

            //trie les éléments dans l'ordre croissant
            Set<String> solution = new TreeSet<>();

            for (int i = 0; i < poids.size(); i++) {
                if(poids.get(i)==minPoids) {
                    solution.add(dico.get(i));
                }
            }
            return new ArrayList<>(solution).get(0);
        }


        private static List<Integer> getPoids(String word, List<String> dico) {
            List<Integer> poids = new ArrayList<>();
            for (String s : dico) {
                poids.add(getPoids(word,s));
            }
            return poids;
        }

        private static Integer getPoids(String word, String s) {
            DamerauLevenshteinAlgorithm algo = new DamerauLevenshteinAlgorithm(2, 2, 3, 3);
            return algo.execute(word, s);
        }



        /**
         * The Damerau-Levenshtein Algorithm is an extension to the Levenshtein
         * Algorithm which solves the edit distance problem between a source string and
         * a target string with the following operations:
         *
         * <ul>
         * <li>Character Insertion</li>
         * <li>Character Deletion</li>
         * <li>Character Replacement</li>
         * <li>Adjacent Character Swap</li>
         * </ul>
         *
         */
        public static class DamerauLevenshteinAlgorithm {
            private final int deleteCost, insertCost, replaceCost, swapCost;

            /**
             * Constructor.
             *
             * @param deleteCost
             *          the cost of deleting a character.
             * @param insertCost
             *          the cost of inserting a character.
             * @param replaceCost
             *          the cost of replacing a character.
             * @param swapCost
             *          the cost of swapping two adjacent characters.
             */
            public DamerauLevenshteinAlgorithm(int deleteCost, int insertCost,
                                               int replaceCost, int swapCost) {
                /*
                 * Required to facilitate the premise to the algorithm that two swaps of the
                 * same character are never required for optimality.
                 */
                if (2 * swapCost < insertCost + deleteCost) {
                    throw new IllegalArgumentException("Unsupported cost assignment");
                }
                this.deleteCost = deleteCost;
                this.insertCost = insertCost;
                this.replaceCost = replaceCost;
                this.swapCost = swapCost;
            }

            /**
             * Compute the Damerau-Levenshtein distance between the specified source
             * string and the specified target string.
             */
            public int execute(String source, String target) {
                if (source.length() == 0) {
                    return target.length() * insertCost;
                }
                if (target.length() == 0) {
                    return source.length() * deleteCost;
                }
                int[][] table = new int[source.length()][target.length()];
                Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();
                if (source.charAt(0) != target.charAt(0)) {
                    table[0][0] = Math.min(replaceCost, deleteCost + insertCost);
                }
                sourceIndexByCharacter.put(source.charAt(0), 0);
                for (int i = 1; i < source.length(); i++) {
                    int deleteDistance = table[i - 1][0] + deleteCost;
                    int insertDistance = (i + 1) * deleteCost + insertCost;
                    int matchDistance = i * deleteCost
                            + (source.charAt(i) == target.charAt(0) ? 0 : replaceCost);
                    table[i][0] = Math.min(Math.min(deleteDistance, insertDistance),
                            matchDistance);
                }
                for (int j = 1; j < target.length(); j++) {
                    int deleteDistance = (j + 1) * insertCost + deleteCost;
                    int insertDistance = table[0][j - 1] + insertCost;
                    int matchDistance = j * insertCost
                            + (source.charAt(0) == target.charAt(j) ? 0 : replaceCost);
                    table[0][j] = Math.min(Math.min(deleteDistance, insertDistance),
                            matchDistance);
                }
                for (int i = 1; i < source.length(); i++) {
                    int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0
                            : -1;
                    for (int j = 1; j < target.length(); j++) {
                        Integer candidateSwapIndex = sourceIndexByCharacter.get(target
                                .charAt(j));
                        int jSwap = maxSourceLetterMatchIndex;
                        int deleteDistance = table[i - 1][j] + deleteCost;
                        int insertDistance = table[i][j - 1] + insertCost;
                        int matchDistance = table[i - 1][j - 1];
                        if (source.charAt(i) != target.charAt(j)) {
                            matchDistance += replaceCost;
                        } else {
                            maxSourceLetterMatchIndex = j;
                        }
                        int swapDistance;
                        if (candidateSwapIndex != null && jSwap != -1) {
                            int iSwap = candidateSwapIndex;
                            int preSwapCost;
                            if (iSwap == 0 && jSwap == 0) {
                                preSwapCost = 0;
                            } else {
                                preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
                            }
                            swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost
                                    + (j - jSwap - 1) * insertCost + swapCost;
                        } else {
                            swapDistance = Integer.MAX_VALUE;
                        }
                        table[i][j] = Math.min(Math.min(Math
                                .min(deleteDistance, insertDistance), matchDistance), swapDistance);
                    }
                    sourceIndexByCharacter.put(source.charAt(i), i);
                }
                return table[source.length() - 1][target.length() - 1];
            }

    }


}
