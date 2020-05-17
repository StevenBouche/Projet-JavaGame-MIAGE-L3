package share.choice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.ressource.TypeRessource;

import java.util.EnumMap;

class ChoiceBetweenRessourceTest {
    static ChoiceBetweenRessource choice;
    static EnumMap<TypeRessource,Integer> listRessource;

    @BeforeEach
    void initTest(){
        listRessource = new EnumMap<>(TypeRessource.class);
        listRessource.put(TypeRessource.GOLD,5);
        choice = new ChoiceBetweenRessource(listRessource,TypeRessource.GOLD);
    }

    @Test
    void getTypeRessource() {
       Assertions.assertEquals(TypeRessource.GOLD,choice.getTypeRessource());

    }

    @Test
    void setTypeRessource() {
        choice.setTypeRessource(TypeRessource.GOLD);
        Assertions.assertEquals(TypeRessource.GOLD,choice.getTypeRessource());
    }

    @Test
    void getListRessource() {
        for(EnumMap.Entry<TypeRessource,Integer> lr: listRessource.entrySet()){
            TypeRessource key = lr.getKey();
            Integer value = lr.getValue();
            EnumMap<TypeRessource,Integer> list = choice.getListRessource();
            Assertions.assertEquals(value.toString(),list.get(key).toString());
        }
    }
}