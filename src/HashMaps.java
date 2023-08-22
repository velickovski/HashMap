import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}
class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {        // Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }
        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}

public class HashMaps {
    public static void main(String[] args) throws InterruptedException, IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        int zapisi = Integer.parseInt(bf.readLine());//4

        CBHT<String ,String > table = new CBHT<>(zapisi*4);

        for(int i=0;i<zapisi;i++) {
            String rechenica = bf.readLine();//"cat machka"
            String[] pomNiza = rechenica.split(" ");
            table.insert(pomNiza[1], pomNiza[0]);
            table.insert(pomNiza[0], pomNiza[1]);
            if (pomNiza[1].startsWith("+")) {
                long broj = Long.parseLong(pomNiza[1]);
                broj = broj % 100000000;
                pomNiza[1] = String.valueOf(broj);
                pomNiza[1] = "0" + pomNiza[1];
            }
            else if (pomNiza[1].startsWith("0"))
            {
                long broj = Long.parseLong(pomNiza[1]);
                broj = broj % 100000000;
                pomNiza[1] = String.valueOf(broj);
                pomNiza[1] = "+389" + pomNiza[1];
            }
            table.insert(pomNiza[1],pomNiza[0]);
            table.insert(pomNiza[0],pomNiza[1]);
        }
        int counter = 0;
        while (true)
        {
            //machka
            if (counter == zapisi)
                break;

            String zbor=bf.readLine();

            SLLNode<MapEntry<String,String>> current=table.search(zbor);

            if(current==null)
            {
                System.out.println("Not found number");
            }
            else
            {
                System.out.println(current.element.value);
            }
            counter++;
        }

    }
}
