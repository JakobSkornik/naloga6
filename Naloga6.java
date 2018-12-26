import java.io.*;


//java Naloga6 C:\Users\User\Desktop\test.txt C:\Users\User\Desktop\out.txt

class Struct {
    
    //struct contains array of Cities (objects) as a Map, array of visited cities for recursion and a number that increments
    //whenever a viable path is found

    public int[] mapa_poti;
    private Mapa m;
    public int rezultat;

    class Mesto {
        
        //each city has an id and a list of connections

        int id;
        Povezava prva;

        Mesto (int id, Povezava prva) {

            this.id = id;
            this.prva = prva;
        }
    }

    class Povezava {
        
        //connections connect two cities and have a height condition, also next pointer

        Mesto p_mesto;
        Mesto d_mesto;
        int predor;
        Povezava next;

        Povezava (Mesto p_mesto, Mesto d_mesto, int predor, Povezava next) {

            this.p_mesto = p_mesto;
            this.d_mesto = d_mesto;
            this.predor = predor;
            this.next = next;
        }
    }

    class Mapa 
        
        //array of cities

        Mesto[] karta;

        Mapa (Mesto[] karta) {

            this.karta = karta;
        }
    }

    public void init (int st_poti) {

        Mesto[] mesteee = new Mesto[st_poti + 1];
        m = new Mapa (mesteee);
        mapa_poti = new int[st_poti + 1];
        rezultat = 0;
    }

    public void vnesi (String ukaz) {
        
        //input is processed here
        //if a city, from input string, isn't present, it is created here  
        
        String[] p = ukaz.split(",");

        Povezava prazna = new Povezava(null, null, -1, null);
        Povezava prazna_2 = new Povezava(null, null, -1, null);

        if (m.karta[Integer.parseInt(p[0])] == null)
            m.karta[Integer.parseInt(p[0])] = new Mesto (Integer.parseInt(p[0]), prazna);

        if (m.karta[Integer.parseInt(p[1])] == null)
            m.karta[Integer.parseInt(p[1])] = new Mesto (Integer.parseInt(p[1]), prazna_2);

        //connection is added to cities here
        
        dodaj_povezavo (m.karta[Integer.parseInt(p[0])], m.karta[Integer.parseInt(p[1])], Integer.parseInt(p[2]));
    }

    public void dodaj_povezavo (Mesto prvo, Mesto drugo, int predor) {
        
        //two unique connections are created and added to the list

        Povezava p = new Povezava(prvo, drugo, predor, null);
        Povezava pp = new Povezava(prvo, drugo, predor, null);

        p.next = prvo.prva.next;
        prvo.prva.next = p;

        pp.next = drugo.prva.next;
        drugo.prva.next = pp;
    }

    public boolean overEnd (Povezava p) {return p == null;}

    public void izpisi_mapo () {

        //for debugging purposes
        
        for (Mesto mesto : m.karta) {

            if (mesto != null) {

                System.out.println(mesto.id);
                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _");

                for (Povezava p = mesto.prva.next; !overEnd(p); p = p.next)
                    System.out.printf("%d,%d,%d\n", p.p_mesto.id, p.d_mesto.id, p.predor);

                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _");
            }
        }
    }

    public void poti (int trenutno, int koncno, int visina) {
        
        //this function goes through all possible paths from starting city to the destination

        
        //if a connection is found
        
        if (trenutno == koncno) {

            rezultat++;
            return;
        }

        //for loop through all connections of the city (trenutno... current city id)
        
        for (Povezava p = m.karta[trenutno].prva.next; !overEnd(p); p = p.next) {
            
            //if a condition is present it used here

            if (p.predor >= visina || p.predor == -1) {

                int kam = p.p_mesto.id;

                if (kam == trenutno)
                    kam = p.d_mesto.id;

                //array of visited cities moves the recursion only through unvisited cities
                
                if (mapa_poti[kam] == 0) {

                    mapa_poti[kam] = 1;

                    poti(kam, koncno, visina);

                    mapa_poti[kam] = 0;
                }
            }
        }
    }
}

public class Naloga6 {

    public static void main(String[] args) throws IOException{

        if (args.length == 2) {

            final long st = System.currentTimeMillis();

            BufferedReader vhod = new BufferedReader(
                    new FileReader(args[0])
            );

            PrintWriter izhod = new PrintWriter(
                    new FileWriter(args[1])
            );

            Struct struct = new Struct();

            int st_poti = Integer.parseInt(vhod.readLine());

            struct.init(st_poti);

            for (int i = 0; i < st_poti; i++)
                struct.vnesi(vhod.readLine());

            String[] pot = vhod.readLine().split(",");
            int h = Integer.parseInt(vhod.readLine());

            struct.mapa_poti[Integer.parseInt(pot[0])] = 1;

            struct.poti(Integer.parseInt(pot[0]), Integer.parseInt(pot[1]), h);

            izhod.print (struct.rezultat);

            final long et = System.currentTimeMillis();

            System.out.println("Cas: " + (et - st));

            vhod.close();
            izhod.close();
        }
    }
}
