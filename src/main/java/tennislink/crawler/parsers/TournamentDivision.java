package tennislink.crawler.parsers;

public enum TournamentDivision {

    AllDivision(""),
    Singles("G1"),
    Doubles("G2"),
    BoysDivisions("G3"),
    GirlsDivisions("G4"),
    MensDivisions("G5"),
    WomensDivisions("G6"),
    MixedDivisions("G7"),
    JuniorDivisions("G8"),
    AdultDivisions("G9"),
    JuniorSingles("G16"),
    Boys18Singles("D1001"),
    Boys16Singles("D1003"),
    Boys14Singles("D1005"),
    Boys12Singles("D1007"),
    Boys10Singles("D1009"),
    Boys8Singles("D1011"),
    Girls18Singles("D1015"),
    Girls16Singles("D1017"),
    Girls14Singles("D1019"),
    Girls12Singles("D1021"),
    Girls10Singles("D1023"),
    Girls8Singles("D1025"),
    Boys18Doubles("D1101"),
    Boys16Doubles("D1103"),
    Boys14Doubles("D1105"),
    Boys12Doubles("D1107"),
    Boys10Doubles("D1109"),
    Boys8Doubles("D1111"),
    Girls18Doubles("D1115"),
    Girls16Doubles("D1117"),
    Girls14Doubles("D1119"),
    Girls12Doubles("D1121"),
    Girls10Doubles("D1123"),
    Girls8Doubles("D1125"),
    Mixed18Doubles("D1130"),
    Mixed16Doubles("D1131"),
    Mixed14Doubles("D1132"),
    Mixed12Doubles("D1133"),
    Mixed10Doubles("D1134"),
    Mixed8Doubles("D1135"),
    FamilyDoubles("G27"),
    FatherSonDoubles("D7100"),
    FatherDaughterDoubles("D7101"),
    MotherSonDoubles("D7102"),
    MotherDaughterDoubles("D7103");



    private String value;
    TournamentDivision(String val){
        value = val;
    }

    public String getValue() {
        return value;
    }
}
