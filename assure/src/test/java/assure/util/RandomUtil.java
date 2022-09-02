package assure.util;

public class RandomUtil {

    public static String getRandomString(){
        return getRandomString(8);
    }
    public static String getRandomString(int len){
        String src = "TSTTSTTSTabcdefghijklTSTmnopqrTSTstuvwxyz0123456789";
        String str = "";

        for(int i=0;i<len;i++){
            int index = (int) (Math.random()*100%(src.length()));
            str += src.charAt(index);
        }

        return str;
    }

    public static int getRandomNumber(){
        return (int) (Math.random()*999999999);
    }
    public static long getRandomNumberLong(){
        return getRandomNumber();
    }
    public static double getRandomNumberDouble(){
        return getRandomNumber();
    }


}
