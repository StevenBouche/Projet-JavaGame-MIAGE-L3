package share.forge;

import share.exeption.InstanceFaceOutOfBoundException;

import java.util.HashMap;
import java.util.Map;


/**
 * The type Factory share.forge.
 */
public class FactoryForge {

    /**
     * Create pool hashtable.
     *
     * @return the hashtable
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public Map<Integer,Pool> createPool() throws InstanceFaceOutOfBoundException {

        Pool p2 = new Pool(2);
        Pool p3 = new Pool(3);
        Pool p4 = new Pool(4);
        Pool p5 = new Pool(5);
        Pool p6 = new Pool(6);
        Pool p8 = new Pool(8);
        Pool p12= new Pool(12);
        HashMap<Integer,Pool> numbers = new HashMap<>();
        numbers.put(p2.getCost(),  p2);
        numbers.put(p3.getCost(), p3);
        numbers.put(p4.getCost(), p4);
        numbers.put(p5.getCost(), p5);
        numbers.put(p6.getCost(), p6);
        numbers.put(p8.getCost(),p8);
        numbers.put(p12.getCost(), p12);
        return numbers;
    }

}