package es.unex.giiis.asee.spanishweather.datosestadisticos

    val provinciaBadajoz = DummyProvincia("Provincia de Badajoz",listOf("Badajoz",
        "Merida Extremadura", "Don Benito", "Villanueva de la Serena",
        "Almendralejo", "Zafra", "Montijo Extremadura", "Villafranca de los Barros",
        "Campanario", "Llerena"))

    val provinciaCaceres = DummyProvincia("Provincia de Caceres",listOf("Caceres",
        "Plasencia", "Coria Extremadura", "Miajadas",
        "Navalmoral de la Mata", "Montehermoso", "Valencia de Alcantara", "Hervás",
        "Jarandilla de la Vera", "Cuacos de Yuste"))

// **************************************************************************************************************
// --------------------------------------------------------------------------------------------------------------
// **************************************************************************************************************

    val provinciaLugo = DummyProvincia("Provincia de Lugo",listOf("Lugo",
        "Monforte de Lemos", "Vivero Galicia", "Villalba Galicia",
        "Sarria", "Foz Galicia", "Ribadeo", "Burela Galicia",
        "Chantada", "Guitiriz"))

    val provinciaOrense = DummyProvincia("Provincia de Orense",listOf("Orense",
        "Carballino", "Verin", "El Barco de Valdeorras",
        "Barbadanes", "Ginzo de Limia", "Pereiro de Aguiar", "Allariz",
        "Celanova", "San Ciprián de Viñas Galicia"))

    val provinciaPontevedra = DummyProvincia("Provincia de Pontevedra",listOf("Vigo",
        "Pontevedra", "Villagarcia de Arosa", "Redondela",
        "Cangas de Morrazo", "Marin Galicia", "Puenteareas", "Porrino",
        "Lalin", "La Estrada"))

    val provinciaCoruna = DummyProvincia("Provincia de Coruña",listOf("A Coruna",
        "Santiago de Compostela", "Ferrol", "Narón",
        "Oleiros", "Arteijo", "Ames Galicia", "Carballo",
        "Culleredo", "Riveira"))

// **************************************************************************************************************
// --------------------------------------------------------------------------------------------------------------
// **************************************************************************************************************

    val provinciaZaragoza = DummyProvincia("Provincia de Zaragoza", listOf(
    "Zaragoza", "Calatayud", "Utebo", "Ejea de los Caballeros", "Cuarte de Huerva",
    "Tarazona", "Caspe", "Zuera", "La Almunia de Doña Godina", "Tauste"))

    val provinciaHuesca = DummyProvincia("Provincia de Huesca", listOf(
    "Huesca", "Monzón", "Barbastro", "Fraga", "Jaca",
    "Binéfar", "Sabiñánigo", "Alquezar", "Tamarite de Litera", "Graus"))

    val provinciaTeruel = DummyProvincia("Provincia de Teruel", listOf(
    "Teruel", "Alcañiz", "Calamocha", "Calanda", "Alcorisa",
    "Utrillas", "Cella", "Monreal del Campo", "Valderrobres", "Albalate del Arzobispo"))

// **************************************************************************************************************
// --------------------------------------------------------------------------------------------------------------
// **************************************************************************************************************

    val provinciaBarcelona = DummyProvincia("Provincia de Barcelona", listOf(
    "Barcelona", "L'Hospitalet de Llobregat", "Terrassa",
    "Badalona", "Sabadell", "Mataró",
    "Santa Coloma de Gramenet", "Sant Cugat del Vallès",
    "Cornella de Llobregat", "San Baudilio de Llobregat"))

    val provinciaGirona = DummyProvincia("Provincia de Girona", listOf(
    "Girona", "Figueras", "Blanes", "Lloret de Mar", "Olot",
    "Salt", "Palafrugell", "Sant Feliu de Guíxols", "Bañolas", "Rosas Catalonia"))

    val provinciaLerida = DummyProvincia("Provincia de Lérida", listOf(
    "Lérida", "Tarrega", "Balaguer", "Mollerusa", "Seo de Urgel",
    "Alcarrás", "Cervera", "Solsona", "Guisona", "Almacellas"))








    val extremadura = DummyRegion("Extremadura",listOf(provinciaBadajoz, provinciaCaceres))
    val galicia = DummyRegion("Galicia", listOf(provinciaLugo, provinciaOrense, provinciaPontevedra, provinciaCoruna))
    val cataluna = DummyRegion("Catalonia", listOf(provinciaBarcelona,provinciaGirona,provinciaLerida))
    val aragon = DummyRegion("Aragon", listOf(provinciaZaragoza,provinciaHuesca,provinciaTeruel))

