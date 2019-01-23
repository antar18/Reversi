package reversi;

import java.util.Scanner;

public class Reversi {

    static final int LIGNES = 8;
    static final int COLONNES = 8;
    static final String NOIR = "noir", BLANC = "blanc", VIDE = "vide";
    static String[][] plateau = new String[LIGNES][COLONNES];
    static boolean[][] casesAChanger = new boolean[LIGNES][COLONNES];
    static String joueurNoir, joueurBlanc, couleurActuelle;
    static int scoreNoir, scoreBlanc, nbrPartiesNoir, nbrPartiesBlanc;
    static boolean continu = true;
    static Scanner reader = new Scanner(System.in);


    public static void main(String[] args) {
      System.out.print("Nom du joueur noir: ");
        joueurNoir = reader.nextLine();

        System.out.print("Nom du joueur blanc: ");
        joueurBlanc = reader.nextLine();

        nbrPartiesBlanc=0;
        nbrPartiesNoir=0;

        debutPartie();

    }

    public static void debutPartie(){
        continu = true;
        scoreNoir =0;
        scoreBlanc=0;
        couleurActuelle = NOIR;

        initialiserPlateaux();
        afficherPlateau();

        while(continu){
            System.out.println("while");
            continu = false;
            if(peutJouer(couleurActuelle)){
                continu = true;
                System.out.println("Tour du joueur "+ couleurActuelle);
                proposerChoix();
                couleurActuelle = contraire(couleurActuelle);
                afficherPlateau();
            }
            else if(peutJouer(contraire(couleurActuelle))){
                continu = true;
                couleurActuelle = contraire(couleurActuelle);
                System.out.println("Le joueur "+couleurActuelle+" ne peut pas jouer, le joueur "+contraire(couleurActuelle)+" continue");
                proposerChoix();
                couleurActuelle = contraire(couleurActuelle);
                afficherPlateau();
            }
            else {
                finPartie();
            }

        }
    }

    //Initialise le plateau de jeu et un tableau intermediaire
    public static void initialiserPlateaux(){
        for(int i =0; i<LIGNES; i++){
            for(int j=0; j<COLONNES; j++){
                plateau[i][j] = VIDE;
                casesAChanger[i][j] = false;
            }
        }
        plateau[3][3] = NOIR;
        plateau[4][3] = NOIR;
        plateau[3][4] = BLANC;
        plateau[4][4] = BLANC;
    }

    public static void afficherPlateau(){
        System.out.println("  a b c d e f g h");
        for(int i =0; i<LIGNES; i++){
            System.out.print(i+1+"|");
            for(int j=0; j<COLONNES; j++){
                if(plateau[i][j].equals(NOIR)) System.out.print("x|");
                else if(plateau[i][j].equals(BLANC)) System.out.print("o|");
                else System.out.print(" |");
            }
            System.out.println("\n------------------");
        }
        System.out.println("\n");
    }

    public static boolean peutJouer(String couleur){
        int a,b;
        for(int i=0; i<LIGNES; i++){
            for(int j=0; j<COLONNES; j++){
                if(plateau[i][j].equals(couleur)){
                    for(int k=-1; k<2; k++){
                        for(int l=-1; l<2; l++){
                            a = i; b = j;                         
                            if(dansTableau(a,b,k,l)){
                            	a = a+k;
                                b = b+l;
                            }
                            while(dansTableau(a,b,k,l) && plateau[a][b].equals(contraire(couleur))){
                                a = a+k;
                                b = b+l;
                            }
                            if(plateau[a][b].equals(VIDE)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void proposerChoix(){
        boolean caseCorrecte=false, caseValide=false;
        char colonne;
        Integer ligne;
        System.out.print("Sélectionnez une case: ");
        Scanner sc = new Scanner (System.in);
        while(!caseCorrecte || !caseValide){
            caseCorrecte = true;
            caseValide = true;

            String choix = sc.nextLine();
            if(choix.length() != 2) caseCorrecte = false;

            colonne = choix.charAt(0);
            if ("abcdefgh".indexOf(colonne) == -1) caseCorrecte = false;

            ligne = Character.getNumericValue(choix.charAt(1));
            if(ligne>8 || ligne < 1) caseCorrecte = false;

            if(!caseCorrecte) System.out.print("Veuillez choisir une case correcte: ");
            else{
                if(caseValide(ligne-1, recupererColonne(colonne))){
                    selectionnerCase(ligne-1, recupererColonne(colonne));
                }
                else {
                    System.out.print("La case n'est pas valide, votre choix: ");
                    caseValide = false;
                }
            }
        }
    }

    public static int recupererColonne(char c){
        if(c=='a') return 0;
        else if(c=='b') return 1;
        else if(c=='c') return 2;
        else if(c=='d') return 3;
        else if(c=='e') return 4;
        else if(c=='f') return 5;
        else if(c=='g') return 6;
        else return 7;
    }

    public static boolean caseValide(int i, int j){
        boolean valide;
        boolean caseValide = false;
        if(!plateau[i][j].equals(VIDE)) return false;
        int a = i;
        int b = j;
        for(int k=-1; k<2;k++){
            for(int l=-1;l<2;l++){
                valide = false;
                if(dansTableau(a,b,k,l)){
                    a = a+k;
                    b = b+l;
                }


                while(dansTableau(a,b,k,l) && plateau[a][b].equals(contraire(couleurActuelle))){
                    valide = true;
                    casesAChanger[a][b] = true;
                    a = a+k;
                    b = b+l;
                }
                if(plateau[a][b].equals(couleurActuelle) && valide){
                    caseValide = true;
                    a = i;
                    b = j;
                }
                else{
                    while(a != i || b != j){
                        casesAChanger[a][b] = false;
                        a = a-k; b = b-l;
                    }
                }
            }
        }
        return caseValide;
    }

    public static void selectionnerCase(int ligne, int colonne){
        plateau[ligne][colonne] = couleurActuelle;
        for(int i =0; i<LIGNES; i++) {
            for (int j = 0; j < COLONNES; j++) {
                if(casesAChanger[i][j]){
                    casesAChanger[i][j] = false;
                    plateau[i][j] = couleurActuelle;
                }
            }
        }
    }

    public static void finPartie(){
        System.out.println("fin");
        for(int i=0; i< LIGNES; i++){
            for(int j=0; j<COLONNES; j++){
                if(plateau[i][j].equals(NOIR)) scoreNoir++;
                else if(plateau[i][j].equals(BLANC)) scoreBlanc++;
            }
        }
        System.out.println(joueurNoir+" "+scoreNoir+ " points");
        System.out.println(joueurBlanc+" "+scoreBlanc+" points");
        if(scoreNoir>scoreBlanc){
            System.out.println(joueurNoir+" est le gagnant");
            nbrPartiesNoir++;
        }
        else if(scoreNoir<scoreBlanc){
            System.out.println(joueurBlanc+" est le gagnant");
            nbrPartiesBlanc++;
        }

        System.out.println(joueurNoir+ ": "+nbrPartiesNoir+" parties gagnées");
        System.out.println(joueurBlanc+ ": "+nbrPartiesBlanc+" parties gagnées");

        int n=0;
        do {
            System.out.println("Tapez 1 pour rejouez ou 2 pour quitter");
            n = reader.nextInt();
            if(n==1) debutPartie();
        }while(n!=1 && n!=2);

    }

    public static String contraire(String couleur){
        if(couleur.equals(NOIR)) return BLANC;
        else if(couleur.equals(BLANC)) return NOIR;
        else return VIDE;
    }

    public static boolean dansTableau(int a, int b, int k, int l){
    	boolean x = a+k >= 0 && a+k <LIGNES && b+l>=0 && b+l<COLONNES;
    	return x;
    	
    }
}
