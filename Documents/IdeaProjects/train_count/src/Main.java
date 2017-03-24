import java.io.*;

public class Main {

    public static void main(String[] args)throws IOException{


        DataOutputStream outputStream  = null;
        InputStream inputStream = null;
        String[][] train = new String[13437][2];
        String[] data = new String[30000];

        try{
            inputStream = new FileInputStream("/Users/xutianyu/Python/edit_distance/train.txt");
            //outputStream = new DataOutputStream(new FileOutputStream("/Users/xutianyu/Python/edit_distance/train_eval.txt"));
            File f = new File("/Users/xutianyu/Python/edit_distance/names.txt");

            //f.getParentFile().mkdirs();
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            //inputStream.read(bytes);
            int byteread = 0 ;
            while ((byteread = inputStream.read(bytes)) != -1){
                //    outputStream.write(bytes, 0, byteCount);
                String str = new String(bytes);
                String[] temp = str.split("\n");
                for(int i = 0 ; i < temp.length; ++i){
                    String[] temp2= temp[i].split("\t");
                    //System.out.println(temp.length);
                    train[i][0] = temp2[0];
                    train[i][1] = temp2[1].replace("\r","");
                    //System.out.println(train[0][0]);

                }
                //System.out.println(train.length);

            }
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(f));
            String tempString = null ;
            int line = 0 ;

            while ((tempString = reader.readLine())!= null){
                data[line]  = tempString ;
                //System.out.println(data[line]+",,,"+data[line]);
                line++;

            }
            //System.out.println(global_distance("cart","arts"));
            //System.out.println(ngram(2,"banana","anana"));
            //write into text
            BufferedWriter out = new BufferedWriter(new
                    FileWriter("/Users/xutianyu/Python/edit_distance/train_count_1.txt"));
            int match = 0;
            //System.out.println(soundex("smith"));
            int[][] weight = new int[26][26];
            int[] sum = new int[26];

            for(int k = 0; k<train.length; ++k){
                String beststr = null;
                int bestindex = 0;
                int dis = 100;
                int count = 0;

                int value = global_distance(train[k][0].toLowerCase(),train[k][1],weight);

                //out.write(match+" "+train.length);
            }
            System.out.print("  ");
            for (int i = 0; i < 26; i++) {
                char c = (char)(i+97);
                System.out.print(c+"    ");

            }
            System.out.print("\n");

            for (int i = 0; i < 26; i++) {
                char c = (char)(i+97);
                System.out.print(c+":");
                for (int j = 0; j < 26; j++) {
                    System.out.print((weight[i][j]+"    ").substring(0,5));
                    sum[i]+=weight[i][j];

                }
                System.out.print("\n");


            }

            System.out.print("  ");
            for (int i = 0; i < 26; i++) {
                char c = (char)(i+97);
                System.out.print(c+"    ");

            }
            System.out.print("\n");

            for (int i = 0; i < 26; i++) {
                char c = (char)(i+97);
                System.out.print(c+":");
                for (int j = 0; j < 26; j++) {

                    int ratio = 0;
                    if(sum[i] != 0)
                            ratio = weight[i][j]*10/sum[i]+1;
                    System.out.print((ratio+"    ").substring(0,5));

                }
                System.out.print("\n");


            }
            System.out.print(match+" "+train.length);
            out.write(match+" "+train.length);
            out.flush();
            out.close();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public static int global_distance(String s1, String s2, int[][] weight){
        int match = 0;
        int insertion = 1;
        int deletion = 1;
        int replace = 1;

        int[][] m = new int[s1.length()][s2.length()];
        for(int i = 0 ;i < s1.length();++i){
            m[i][0] = i;
            for(int j = 0; j< s2.length(); ++j){
                m[0][j] = j ;
            }
        }
        for(int i = 1; i<s1.length(); ++i){
            for(int j = 1; j< s2.length(); ++j){
                if (s1.charAt(i) == s2.charAt(j)){
                    m[i][j] = m[i-1][j-1]+match;
                    /*
                    if( (j+1)<s2.length()&&s1.charAt(i)=='l') {
                        if (s2.charAt(j + 1) == 'l' || s2.charAt(j - 1) == 'l') {
                            m[i][j] = m[i - 1][j - 1] - 1;
                        }
                    }
                    else if(s2.charAt(j - 1) == 'l'){
                        m[i][j] = m[i - 1][j - 1] - 1;
                    }
                    */
                    /*
                    if(s1.charAt(i)=='r'&& j>=1){
                        if(s2.charAt(j-1)=='e'){
                            m[i][j] = m[i][j] -1;
                        }
                    }
                    */

                }
                else{
                    if(m[i-1][j]+1 >m[i][j-1]+1){
                        m[i][j] = m[i][j-1] +1;
                    }
                    else
                        m[i][j] = m[i-1][j] +1;
                    if(m[i][j] > m[i - 1][j - 1] + 1){
                            m[i][j] = m[i - 1][j - 1]+1;
                            int index1 = s1.charAt(i)-97;
                            int index2 = s2.charAt(j)-97;
                            if(index1 >-1) {
                                weight[index1][index2]++;
                            }
                    }

                }
            }
        }



        return m[s1.length()-1][s2.length()-1];
    }


    public static int ngram(int k, String s1, String s2) {

        s1 = "#"+s1+"#";
        s2 = "#"+s2+"#";
        int count = 0;
        int[] flag = new int[s2.length()-k+1];
        for(int i = 0; i< s1.length()-k+1; ++i ){
            for(int j = 0; j<s2.length()-k+1 ; ++j) {
                if (s1.substring(i, i + k).equals(s2.substring(j, j + k)) && flag[j]!=1){
                    count++;
                    flag[j] = 1;
                }
                //System.out.println(s1.substring(i, i + k)+",,,"+s2.substring(j, j + k));
            }

        }

        return (s1.length()-k+1+s2.length()-k+1-2*count);
    }

    public static String soundex(String s){

        char[] x = s.toUpperCase().toCharArray();
        char first_letter = x[0];

        for (int i = 0; i < x.length; ++i) {
            switch (x[i]) {
                case 'B':
                case 'F':
                case 'P':
                case 'V': {
                    x[i] = '1';
                    break;
                }

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z': {
                    x[i] = '2';
                    break;
                }
                case 'D':
                case 'T': {
                    x[i] = '3';
                    break;
                }

                case 'L': {
                    x[i] = '4';
                    break;
                }

                case 'M':
                case 'N': {
                    x[i] = '5';
                    break;
                }

                case 'R': {
                    x[i] = '6';
                    break;
                }

                default: {
                    x[i] = '0';
                    break;
                }
            }

        }
        String out = ""+first_letter;
        for(int i = 1 ; i< x.length;++i){
            if(x[i]!=x[i-1]&&x[i]!= '0'){
                out = out +x[i];
            }
        }
        return out;
    }


    public static int soundex_eval(char c) {
        String s = c+"";
        s = s.toUpperCase();
        c = s.charAt(0);
        int value = 0;
        switch (c) {
            case 'B':
            case 'F':
            case 'P':
            case 'V': {
                value = 1;
                break;
            }

            case 'C':
            case 'G':
            case 'J':
            case 'K':
            case 'Q':
            case 'S':
            case 'X':
            case 'Z': {
                value = 2;
                break;
            }
            case 'D':
            case 'T': {
                value = '3';
                break;
            }

            case 'L': {
                value = '4';
                break;
            }

            case 'M':
            case 'N': {
                value = '5';
                break;
            }

            case 'R': {
                value = '6';
                break;
            }

            default: {
                value = '0';
                break;
            }
        }
        return value;

    }
}
