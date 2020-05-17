package share.core;

import share.exeption.CaseOfPoolForgeOutOfBound;
import share.face.Face;

public interface IHandlerForge {

    Face getFaceFromPool(int nbPool, int nbPosFace) throws CaseOfPoolForgeOutOfBound;
}
