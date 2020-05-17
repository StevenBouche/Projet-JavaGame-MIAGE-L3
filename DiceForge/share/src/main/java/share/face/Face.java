package share.face;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The type Face.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Face {

    /**
     * The Type.
     */
    public TypeFace type;

    /**
     * Instantiates a new Face.
     *
     * @param t the t
     */
    public Face(TypeFace t) {
        this.type = t;
    }

}
