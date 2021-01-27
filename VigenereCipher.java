/*
Author: Scott Tabaka
Instructor: Dr. Mark Hauschild
CmpSci 4732 Summer 2019
Project #1
Due Date: 7/7/2019

Purpose: Menu driven Vigenere Cipher to encrypt/decrypt plaintext and bruteforce the ciphertext.
*/

import java.util.Scanner;
import java.util.HashMap;


public class VigenereCipher
{
    private static HashMap<Integer,String> hmitos=new HashMap<>();
    private static HashMap<String,Integer> hmstoi=new HashMap<>();
    private static Scanner scan = new Scanner(System.in);

    private static String encrypt(String pt,String key)
    {
        char[] ptArray = pt.toCharArray();
        int[] ptiArray = new int[ptArray.length];
        char[] keyArray = key.toCharArray();
        int[] keyiArray = new int[ptArray.length];

        for(int i=0;i<ptArray.length;i++)
        {
            ptiArray[i]=hmstoi.get(String.valueOf(ptArray[i]));
        }
        for(int i=0;i<ptArray.length;i++)
        {
            keyiArray[i]=hmstoi.get(String.valueOf(keyArray[(i%keyArray.length)]));
        }
        for(int i=0;i<ptArray.length;i++)
        {
            ptiArray[i]=((ptiArray[i]+keyiArray[i])%26);
        }

        String tempstr;
        for(int i=0;i<ptArray.length;i++)
        {
            tempstr=hmitos.get(ptiArray[i]);
            ptArray[i]=tempstr.charAt(0);
        }

        return String.valueOf(ptArray);
    }

    private static String decrypt(String ct,String key)
    {
        char[] ctArray = ct.toCharArray();
        int[] ctiArray = new int[ctArray.length];
        char[] keyArray = key.toCharArray();
        int[] keyiArray = new int[ctArray.length];

        for(int i=0;i<ctArray.length;i++)
        {
            ctiArray[i]=hmstoi.get(String.valueOf(ctArray[i]));
        }
        for(int i=0;i<ctArray.length;i++)
        {
            keyiArray[i]=hmstoi.get(String.valueOf(keyArray[(i%keyArray.length)]));
        }
        for(int i=0;i<ctArray.length;i++)
        {
            ctiArray[i]=((ctiArray[i]-keyiArray[i])%26);
            if(ctiArray[i]<0)
            {
                ctiArray[i]+=26;
            }
        }

        String tempstr;
        for(int i=0;i<ctArray.length;i++)
        {
            tempstr=hmitos.get(ctiArray[i]);
            ctArray[i]=tempstr.charAt(0);
        }

        return String.valueOf(ctArray);
    }

    private static void bruteforce(String ct,String substr,int maxlen)
    {
        char[] ctArray = ct.toCharArray();
        int[] ctiArray = new int[ctArray.length];
        int[] ctiArray2 = new int[ctArray.length];

        for(int i=0;i<ctArray.length;i++)
        {
            ctiArray[i]=hmstoi.get(String.valueOf(ctArray[i]));
        }

        keySizeLoop:
        for(int keySize=1;keySize<maxlen+1;keySize++)
        {
            System.out.println("List of Matches for key length " + keySize);
            System.out.println("------------------------------");
            int[] keyiArray = new int[ctArray.length];
            int[] bruteKeyiArray = new int[keySize];
            int matchNumber = 0;

            for(int i=0;i<keySize;i++)
            {
                bruteKeyiArray[i]=0;
            }

            while(bruteKeyiArray[0] != 26)
            {
                for(int i=0;i<ctArray.length;i++)
                {
                    keyiArray[i]=(bruteKeyiArray[(i%bruteKeyiArray.length)]);
                }
                for(int i=0;i<ctArray.length;i++)
                {
                    ctiArray2[i]=((ctiArray[i]-keyiArray[i])%26);
                    if(ctiArray2[i]<0)
                    {
                        ctiArray2[i]+=26;
                    }
                }

                String tempstr;
                for(int i=0;i<ctArray.length;i++)
                {
                    tempstr=hmitos.get(ctiArray2[i]);
                    ctArray[i]=tempstr.charAt(0);
                }

                if(String.valueOf(ctArray).contains(substr))
                {
                    char[] keyArray = new char[bruteKeyiArray.length];
                    matchNumber+=1;

                    String key;
                    for(int i=0;i<bruteKeyiArray.length;i++)
                    {
                        key=hmitos.get(bruteKeyiArray[i]);
                        keyArray[i]=key.charAt(0);
                    }

                    System.out.println("Matched plaintext #" + matchNumber + ": " + String.valueOf(ctArray));
                    System.out.println("Matched key #" + matchNumber + ": " + String.valueOf(keyArray) + "\n");

                    if(!continueChoice("Continue searching? (y/n)"))
                    {
                        break keySizeLoop;
                    }
                }

                bruteKeyiArray[keySize-1]+=1;
                for(int i=0;i<keySize-1;i++)
                {
                    if((bruteKeyiArray[keySize-1-i])==26)
                    {
                        bruteKeyiArray[keySize-2-i]+=1;
                        bruteKeyiArray[keySize-1-i]=0;
                    }
                }
            }
            if(matchNumber==0)
            {
                System.out.println("There were no matches found for key length " + keySize + "\n");
            }

            if(keySize<maxlen && !continueChoice("There are no remaining matches for this key length. Increase Key Size by 1 and continue searching? (y/n)"))
            {
                break;
            }
        }
    }

    private static Boolean continueChoice(String str)
    {
        String choice = "";

        while(!choice.equals("y") && !choice.equals("n"))
        {
            System.out.println(str);
            choice = scan.next();
            choice = choice.replaceAll("[^a-zA-Z]", "").toLowerCase();
        }

        return choice.equals("y");
    }

    private static void displayMenuChoices()
    {
        System.out.println("*******Project 1*******");
        System.out.println("*******Main Menu*******");
        System.out.println("1. Task 1(Encrypt/Decrypt)");
        System.out.println("2. Task 2(Brute Force)");
        System.out.println("3. Exit");
        System.out.println("Please make a selection(1-3)");
    }

    private static void mainMenu()
    {
        while(true)
        {
            displayMenuChoices();
            int menuChoice=0;

            while(menuChoice<1 || menuChoice>3)
            {
                while (!scan.hasNextInt())
                {
                    displayMenuChoices();
                    System.out.println("Error, Not a number. Please enter a number(1-3)");
                    scan.next();
                }

                menuChoice = scan.nextInt();

                if (menuChoice<1 || menuChoice>3)
                {
                    displayMenuChoices();
                    System.out.println("Error, Number not 1-3. Please enter a number(1-3)");
                }
            }

            switch(menuChoice)
            {
                case 1:
                    String plaintext = "";
                    String key = "";
                    while(plaintext.length()<1)
                    {
                        System.out.println("Enter plain text string");
                        plaintext = scan.next();
                        plaintext = plaintext.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(plaintext.length()<1)
                        {
                            System.out.println("Error, Please enter atleast 1 alphabetic character");
                        }
                    }
                    while(key.length()<1)
                    {
                        System.out.println("Enter key string");
                        key = scan.next();
                        key = key.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(key.length()<1)
                        {
                            System.out.println("Error, Please enter atleast 1 alphabetic character");
                        }
                    }

                    String ciphertext=encrypt(plaintext,key);
                    System.out.println("\nCipher Text: " + ciphertext);

                    String deciphertext=decrypt(ciphertext,key);
                    System.out.println("Deciphered Text: " + deciphertext + "\n");


                    if(!continueChoice("Return to VigenereCipher Menu? (y/n)"))
                    {
                        scan.close();
                        System.exit(0);
                    }

                    break;
                case 2:
                    String bruteCiphertext = "";
                    String substr = "";
                    int maxKeyLength=0;
                    while(bruteCiphertext.length()<1)
                    {
                        System.out.println("Enter cipher text string");
                        bruteCiphertext = scan.next();
                        bruteCiphertext = bruteCiphertext.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(bruteCiphertext.length()<1)
                        {
                            System.out.println("Error, Please enter atleast 1 alphabetic character");
                        }
                    }
                    while(substr.length()<1)
                    {
                        System.out.println("Enter substring to search for");
                        substr = scan.next();
                        substr = substr.replaceAll("[^a-zA-Z]", "").toLowerCase();
                        if(substr.length()<1)
                        {
                            System.out.println("Error, Please enter atleast 1 alphabetic character");
                        }
                    }
                    while(maxKeyLength<1 || maxKeyLength>10)
                    {
                        System.out.println("Enter maximum possible key size(1-10)");
                        while (!scan.hasNextInt())
                        {
                            System.out.println("Error, Number not 1-10. Please enter a number(1-10)");
                            scan.next();
                        }

                        maxKeyLength = scan.nextInt();

                        if (maxKeyLength<1 || maxKeyLength>10)
                        {
                            System.out.println("Error, Not a number. Please enter a number(1-10)");
                        }
                    }

                    bruteforce(bruteCiphertext,substr,maxKeyLength);

                    if(!continueChoice("Return to VigenereCipher Menu? (y/n)"))
                    {
                        scan.close();
                        System.exit(0);
                    }

                    break;
                case 3:
                    scan.close();
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    private static void initializeHashmap()
    {
        for(int i=0;i<26;i++)
        {
            hmitos.put(i,String.valueOf((char)(97+i)));
            hmstoi.put(String.valueOf((char)(97+i)),i);
        }
    }

    public static void main(String[] args)
    {
        initializeHashmap();
        mainMenu();
    }
}
