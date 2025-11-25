package com.sibelsama.lovelyy5.data

object RegionsData {
    val REGIONS = listOf(
        "I - Región de Tarapacá",
        "II - Región de Antofagasta",
        "III - Región de Atacama",
        "IV - Región de Coquimbo",
        "V - Región de Valparaíso",
        "VI - Región de O'Higgins",
        "VII - Región del Maule",
        "VIII - Región del Biobío",
        "IX - Región de La Araucanía",
        "X - Región de Los Lagos",
        "XI - Región de Aysén",
        "XII - Región de Magallanes",
        "RM - Región Metropolitana",
        "XIV - Región de Los Ríos",
        "XV - Región de Arica y Parinacota",
        "XVI - Región de Ñuble"
    )

    val COMUNAS_BY_REGION: Map<String, List<String>> = mapOf(
        "I - Región de Tarapacá" to listOf(
            "Iquique","Alto Hospicio","Pozo Almonte","Camiña","Colchane","Huara","Pica"
        ),
        "II - Región de Antofagasta" to listOf(
            "Antofagasta","Mejillones","Sierra Gorda","Taltal","Tocopilla","María Elena"
        ),
        "III - Región de Atacama" to listOf(
            "Copiapó","Caldera","Tierra Amarilla","Chañaral","Diego de Almagro","Vallenar","Freirina","Huasco","Alto del Carmen"
        ),
        "IV - Región de Coquimbo" to listOf(
            "La Serena","Coquimbo","Andacollo","La Higuera","Vicuña","Paihuano","Illapel","Salamanca","Los Vilos","Canela","Ovalle","Combarbalá","Monte Patria","Punitaqui","Río Hurtado"
        ),
        "V - Región de Valparaíso" to listOf(
            "Valparaíso","Viña del Mar","Concón","Quintero","Puchuncaví","Casablanca","Algarrobo","El Quisco","El Tabo",
            "Santo Domingo","San Antonio","Cartagena","La Ligua","Cabildo","Zapallar","Papudo","Petorca","Quillota","La Calera",
            "Hijuelas","Nogales","Olmué","Limache","Quilpué","Villa Alemana","Los Andes","San Esteban","Rinconada","Calle Larga",
            "San Felipe","Putaendo","Catemu","Llay Llay","Panquehue","Santa María"
        ),
        "VI - Región de O'Higgins" to listOf(
            "Rancagua","Machalí","Graneros","Requínoa","Coinco","Codegua","Olivar","Rengo","Pichidegua","San Vicente de Tagua Tagua","San Fernando",
            "Santa Cruz","Pumanque","Nancagua","Chépica","La Estrella","Palmilla","Peralillo","Lolol","Mostazal","Coltauco"
        ),
        "VII - Región del Maule" to listOf(
            "Talca","San Clemente","Curepto","Maule","Pencahue","Molina","Río Claro","Constitución","Empedrado","Cauquenes","Chanco","Pelluhue",
            "Curicó","Hualañé","Licantén","Linares","Colbún","Parral","Retiro","Longaví","San Javier","Vichuquén"
        ),
        "VIII - Región del Biobío" to listOf(
            "Concepción","Talcahuano","Hualpén","Chiguayante","Coronel","Lota","Penco","Tome","Hualqui","Santa Juana","Florida","Nacimiento","Los Álamos",
            "Cabrero","Yumbel","Lebu","Arauco","Curanilahue","Tirúa","Cañete","San Pedro de la Paz","Laja","Mulchén"
        ),
        "IX - Región de La Araucanía" to listOf(
            "Temuco","Padre Las Casas","Nueva Imperial","Carahue","Pitrufquén","Vilcún","Victoria","Angol","Renaico","Collipulli","Curacautín","Lonquimay",
            "Melipeuco","Perquenco","Lautaro","Loncoche","Pucón","Gorbea","Cholchol","Freire","Villarrica"
        ),
        "X - Región de Los Lagos" to listOf(
            "Puerto Montt","Calbuco","Maullín","Los Muermos","Frutillar","Llanquihue","Castro","Ancud","Chonchi","Quellón","Quemchi","Dalcahue",
            "Curaco de Vélez","Puqueldón","Quinchao","Osorno","Purranque","Puerto Octay","Río Negro","San Juan de la Costa","Puyehue","Fresia","Puerto Varas"
        ),
        "XI - Región de Aysén" to listOf(
            "Coyhaique","Aysén","Cisnes","Guaitecas","Río Ibáñez","Chile Chico","Cochrane","O'Higgins","Tortel"
        ),
        "XII - Región de Magallanes" to listOf(
            "Punta Arenas","Porvenir","Primavera","Laguna Blanca","Río Verde","San Gregorio","Timaukel","Cabo de Hornos","Laguna Blanca","Puerto Natales","Torres del Paine"
        ),
        "RM - Región Metropolitana" to listOf(
            "Santiago","Cerrillos","Cerro Navia","Conchalí","El Bosque","Estación Central","Huechuraba","Independencia","La Cisterna","La Florida",
            "La Granja","La Pintana","La Reina","Las Condes","Lo Barnechea","Lo Espejo","Lo Prado","Macul","Maipú","Ñuñoa",
            "Pedro Aguirre Cerda","Peñalolén","Providencia","Pudahuel","Quilicura","Quinta Normal","Recoleta","Renca","San Joaquín","San Miguel",
            "San Ramón","Vitacura","Puente Alto","Pirque","San José de Maipo","Colina","Lampa","Tiltil","San Bernardo","Buin","Calera de Tango",
            "Paine","Melipilla","Alhué","Curacaví","María Pinto","Isla de Maipo"
        ),
        "XIV - Región de Los Ríos" to listOf(
            "Valdivia","Corral","Lanco","Los Lagos","Máfil","Mariquina","Panguipulli","Río Bueno","La Unión"
        ),
        "XV - Región de Arica y Parinacota" to listOf(
            "Arica","Camarones","Putre","General Lagos"
        ),
        "XVI - Región de Ñuble" to listOf(
            "Chillán","Chillán Viejo","Bulnes","Pemuco","Coihueco","Quillón","San Carlos","Yungay","Pinto","San Ignacio","Ránquil","Treguaco","Cobquecura","Tucapel"
        )
    )
}
