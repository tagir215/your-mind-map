# YourMindMap
<img width="1000" alt="mindmapkuvat" src="https://user-images.githubusercontent.com/117892331/225334535-7bde7267-faad-462e-b74a-196fc8fd7286.png">

*Java, Android Studio, Xml, Json*

**YourMindMap on ajatuskarttojen luontiin tarkoitettu sovellus. Sovelluksesta löytyy lukuisia työkaluja joissa käyttäjää on huomioitu antamalla hänelle mahdollisimman vapaat kädet. Ajatuskartat sommittautuvat automaattisesti ja niiden soluja on helppoa siirrellä. Yritin tehdä tästä julkaistavan äpin, joten aika suuri osa ajasta meni 90-90 säännön mukaisesti pieniä korjauksia tehdessä**

- Ajatuskartan automaattinen tilan sommittelu. Kokeilin useita versioita, mutta päädyin laskemaan kaikki koordinaatit täysin matemaattisesti ja piirtämään kaiken canvakselle.
- Canvas ei piirrä karttaa joka ruudunpäivityksen jälkeen, vaan vain kun käyttäjä päästää irti esimerkiksi liikutettuuan karttaa. Bitmap itse siirtyy sormen mukana, mutta siirtyy takaisin paikoilleen sormen noustessa, jonka jälkeen piirtää kartan uudelleen, mutta ottaen siirretyn matkan huomioon
- Ajatuskartalla voi tehdä huonommallakin kännykällä yli 5 tuhannen solun karttoja ilman huomattavaa suorituskyvyn laskua.
- Suorakulmio valinta, ja monen solun muokkaaminen kerralla
- Viivan, laatikon, reuonojen muotojen, värien, ja viiva tyyppien editointi työkalut
- Tekstien värien, fonttien, asettelu ja koon editointi työkalut, sekä kartan automaattinen sopeutuminen eri fontti- ja teksti-koko asetuksille
- Tallennus, undo ja redo komennot sekä useiden karttojen tallennus mahdollisuus
- Vienti mahdollisuuksia pdf, jpg tai png
- Zoom
- Haarakkeiden pakkaus työkalu
- Kopionti ja liittämistyökalut
- Keskittämis työkalu
- Kuvien lisäys mahdollisuus
- Opin paljon Android Frameworkin työkaluja sekä Javan- ja olio-ohjelmoinnin perusteita. Aloitin projektin useaan kertaan alusta koska minulle tuli ongelmia suoristuskyvyn ja koodin sekavuuden kanssa. 
- Tässä projektissa opin ainakin miten ei kannata tehdä skaalautuvia äppejä.
