#pragma once
#include <string>
#include <vector>

enum typesSimple {
    EXPLOIT,
    CHOICE_3GOLD_FOR_4GLORY,
    ADD_GOLD_TO_HAMMER,
    POWER_CHOICE,
};

enum typeDouble {
    FORGE,
    HYBRID,
    CHOICE_BETWEEN_RESSOURCES,
    CHOICE_FORGE_FACE_SPECIAL,
    SATYRE_CHOICE,
};

enum noChoice {
    PLACE,
    ONE_MORE_TURN,
    ADD_HAMMER
};

struct SimpleChoice {
    std::string eventType;
    std::string choice;

    SimpleChoice(std::string eventType, std::string choice)
        : eventType(eventType), choice(choice) {}
};

struct DoubleChoice {
    std::string eventType;
    std::string value1;
    std::string value2;

    DoubleChoice(std::string eventType, std::string value1, std::string value2)
        : eventType(eventType), value1(value1), value2(value2) {}
};

struct Player {
    std::vector<std::string> noValueChoices;
    std::vector<SimpleChoice> simpleChoices;
    std::vector<DoubleChoice> doubleChoices;
    std::string id;

    Player(std::string id) : id(id) {}
};

struct ForgeChoice {
    std::string oldFace, newFace;

    ForgeChoice(std::string oldFace, std::string newFace)
        : oldFace(oldFace), newFace(newFace) {}
};

struct HybridChoice {
    std::string type;
    int value;

    HybridChoice(std::string type, int value)
        : type(type), value(value) {}
};

struct ChoiceBetweenRessources {
    std::string type;
    int ammount;

    ChoiceBetweenRessources(std::string type, int ammount)
        : type(type), ammount(ammount) {}
};

struct SatyreChoice {
    std::string face1, face2;

    SatyreChoice(std::string face1, std::string face2)
        : face1(face1), face2(face2) {}
};