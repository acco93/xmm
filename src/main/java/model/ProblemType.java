package model;

public enum ProblemType {
    UNDEFINED{
        public String toString() {
            return "undefined";
        }
    },
    XSTTRP {
        public String toString() {
            return "xsttrp";
        }
    },
    MDVRP{
        public String toString() {
            return "mdvrp";
        }
    },
    LRP{
        public String toString() {
            return "lrp";
        }
    },
    CVRP {
        public String toString() {
            return "cvrp";
        }
    };


}
