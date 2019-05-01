package uc.seng201.crew;

/*
 * The types of Crew Member available to select from.
 */
public enum CrewType {
    CRYSTAL("Crystal") {
        @Override
        public CrewMember getInstance(String name) {
            return new Crystal(name);
        }
    },
    ENGI("Engi") {
        @Override
        public CrewMember getInstance(String name) {
            return new Engi(name);
        }
    },
    HUMAN("Human") {
        @Override
        public CrewMember getInstance(String name) {
            return new Human(name);
        }
    },
    SLUG("Slug") {
        @Override
        public CrewMember getInstance(String name) {
            return new Slug(name);
        }
    },
    LANIUS("Lanius") {
        @Override
        public CrewMember getInstance(String name) {
            return new Lanius(name);
        }
    },
    ZOLTAN("Zoltan") {
        @Override
        public CrewMember getInstance(String name) {
            return new Zoltan(name);
        }
    };

    private String name;

    CrewType(String name) {
        this.name = name;
    }

    public abstract CrewMember getInstance(String name);

    @Override
    public String toString() {
        return this.name;
    }
}
