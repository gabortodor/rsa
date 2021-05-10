import java.util.Random;

public class Rsa {

    private static Random rand = new Random();

    private static int getRandom() {
        return rand.nextInt(3500) + 100;
    }

    private static boolean isPrime(int inputNumber) {
        if (inputNumber <= 3 || inputNumber % 2 == 0) {
            return inputNumber == 2 || inputNumber == 3;
        }
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNumber)) && (inputNumber % divisor != 0)) {
            divisor += 2;
        }
        return inputNumber % divisor != 0;
    }


    private static int getGcd(int a, int b) {
        int temp;
        while (b != 0) {
            temp = a;
            a = b;
            b = temp % b;
        }
        return a;
    }

    private static int phi(int a, int b) {

        return (a - 1) * (b - 1);
    }

    private static int dCheck(int e, int phi) {
        int d = 0;
        for (int i = 1; i < phi; i++) {
            int temp = (phi * i) + 1;
            if (temp % e == 0) {
                d = temp / e;
                break;
            }
        }
        return d;
    }

    private static String getBinary(int a) {
        StringBuilder binary = new StringBuilder();
        int b = a;
        while (b != 0) {
            binary.append((char) b % 2);
            b /= 2;
        }
        return binary.toString();
    }


    private static long moduloCalculator(long c, int d, int n) {
        String binary = getBinary(d);
        long[] mods = new long[binary.length()];

        long mod = 1;
        for (int i = 0; i < binary.length(); i++) {
            if (i == 0) {
                mods[0] = ((long) Math.pow(c, Math.pow(2, 0))) % n;
            } else {
                mods[i] = (mods[i - 1] * mods[i - 1]) % n;
            }
            if (binary.charAt(i) == '1') {
                mod *= mods[i];
                mod %= n;
            }
        }
        return mod;
    }

    private static int encrypt(int message, int e, int n) {
        int result=(int) moduloCalculator(message, e, n);
        System.out.print("\nEncrypted message: "+result);
        return result;
    }

    private static int[] encrypt(String message, int e, int n) {
        int[] chars = new int[message.length()];
        System.out.print("\nEncrypted message: ");
        for (int i = 0; i < message.length(); i++) {
            int c = (int) message.charAt(i);
            long cod = moduloCalculator(c, e, n);
            chars[i] = (int) cod;
            System.out.print(cod+" ");
        }
        return chars;
    }

    private static void decrypt(int[] array, int d, int n) {
        System.out.print("\nDecrypted message: ");
        if (array.length == 1) {
            System.out.print((moduloCalculator(array[0], d, n)));
        } else {
            for (int i = 0; i < array.length; i++) {
                System.out.print((char) moduloCalculator(array[i], d, n));
            }
        }
    }


    public static void main(String[] args) {
        int messageNum = 0;
        boolean asInt;
        if (args.length < 2) {
            System.out.println("Usage: \"Integer/String\" \"message\"");
            return;
        }

        if (args[0].equalsIgnoreCase("Integer")) {
            try {
                messageNum = Integer.parseInt(args[1]);
                asInt = true;
            } catch (NumberFormatException e) {
                asInt = false;
            }
        } else if (args[0].equalsIgnoreCase("String")) {
            asInt = false;
        } else {
            System.out.println("Invalid type parameter or value");
            return;
        }


        int p = getRandom();
        int q = getRandom();

        while (!isPrime(p) || !isPrime(q)) {
            if (!isPrime(p)) {
                p = getRandom();
            }
            if (!isPrime(q)) {
                q = getRandom();
            }
        }

        int n = p * q;
        if (n < messageNum) {
            asInt = false;
        }

        int e = rand.nextInt(100);
        int phi = phi(p, q);
        while (getGcd(e, phi) != 1) {
            e = rand.nextInt(100);
        }

        int d = dCheck(e, phi);

        System.out.println("Public key:(" + e + ", " + n + ")");
        System.out.println("Private key:(" + d + ", " + n + ")");
        System.out.println("Inputted message: " + args[1]);

        if (asInt) {
            int dec = encrypt(messageNum, e, n);
            decrypt(new int[]{dec}, d, n);

        } else {
            int[] encryptedChars = encrypt(args[1], e, n);
            decrypt(encryptedChars,d,n);
        }

    }
}
