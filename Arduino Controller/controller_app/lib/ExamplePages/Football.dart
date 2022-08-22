import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class futlist extends StatefulWidget {
  final String continent;
  const futlist(this.continent) ;
  @override
  _futlistState createState() => _futlistState();
}

final List<String> _stadiumAmerica = <String>['Maracanã - Rio de Janeiro','BC Place - Vancouver','Mercedes Benz - Atlanta','Monumental - Buenos Aires','Mané Garrincha - Brasilia','Monumental de la "U" - Lima','Tomás Adolfo - Buenos Aires','Estadio Alberto .J - Buenos Aires','Unico Ciudad de la PLata - Argentina','Nacional de Chile - Chile','Azteca - Mexico','Libertadores de América - Argentina','Cilindro de Avellaneda - Argentina', 'Jalisco - Mexico','University Stadium - Mexico','Red Bull Arena - New Jersey'];
final List<String> _coordAmerica = <String>['-43.2301558,-22.9121089,963','-123.1109157,49.2760869,660','-84.400855,33.7554483,860','-58.4497749,-34.5453062,851','-47.899211,-15.7835191,1012','-76.9360949,-12.0557045,1030','-58.3965082,-34.6434905,850','-58.3647563,-34.6356109,850','-57.9891083,-34.9137939,847','-70.6106762,-33.4646281,863','-99.1505277,19.3028607,990','-58.3713407,-34.6702197,850','-58.3685984,-34.6674016,850','-103.3281769,20.7049947,980','-100.3120225,25.7225198,940','-74.1502355,40.7368436,777'];

final List<String> _stadiumAfrica= <String>['FNB Stadium/Johannesburg, South Africa','Borg El Arab Stdium/Cairo, Egytp','Stade des Martyrs/Kinshasha, Congo','July 5 Stadium/Algiers, Algeria','Ellis Park Stadium/Johannesburg,South Africa','Abuja Stadium/Abuja, Nigeria','Stade 7 November/Radès, Tunisia','Stade Municipal de Kintélé/Brazzaville,Congo','Bahir Dar Stadium/Bahir Dar,Ethiopia','Tanzania National Main Stadium/Dar es Salaam, Tanzania','Stade Leopold Senghor/Dakar, Senegal','Moi International Sports Centre/Nairobi,Kenya','Heroes National Stadium/Lusaka,Zambia','National Sports Stadium/Harare, Zimbabwe','Odi Stadium/Mabopane,South Africa','Cape Town Stadium/Cape Town, South Africa'];
final List<String> _coordAfrica= <String>['27.9826554,-26.2347569,936.0049263','29.7293881,30.9995782,890.130491','15.3106251,-4.3306807,1053.165127','2.9954651,36.7599253,826.2707395','28.0608802,-26.1975466,936.3369077','7.4533976,9.038055,1041.871455','10.2726928,36.7479797,826.4120692','15.3243828,-4.1548507,1053.433602','37.3826554,11.5846685,485.8574568','39.2738498,-6.8534904,1048.094254','-17.4519086,14.7467034,1017.705709','36.890904,-1.2289973,1056.270149','28.2729442,-15.3697264,1014.387737','30.9963269,-17.8232239,1000.046692','28.0406754,-25.5216085,942.2946379','18.4098451,-33.904791,859.0189854'];

final List<String> _stadiumEurope= <String>['Camp Nou/Barcelona, Spain','Camp dEsports/Lleida, Spain','Stadium Olimpik/Berlin, Germany','Wanda Metropolitano/Madrid, Spain','San Siro/Milano, Italy','Allianz Arena/Munich, Germany','Molde Stadium/Molde, Norway','Red Bull Arena/Salzburg, Austria','Olimpico de Roma/ Roma, Italy','Signal Iduna Park/Dortmund, Germany','Stadion Poljud/Split, Croatia','Juventus Stadium/Torino, Italy','Stadium of light/Sunderlad, England','Old Trafford/Manchester, England','Olympic Stadium of London/London, England','Anfield/Liverpool, England'];
final List<String> _coordEurope= <String>['2.1228198,41.380896,769.028945','0.6142732,41.6212978,765.916128','13.2394987,52.5146971,612.7944244','-3.5994674,40.4361939,781.1361231','9.123962,45.4781236,714.289045','11.6247072,48.2187997,675.798257','7.1480688,62.7334192,452.5915626','12.9982153,47.8162823,681.53918','12.454725,41.934077,761.8470482','7.4518574,51.4925888,628.0588815','16.4318567,43.5196129,740.8944434','7.6412644,45.109569,719.3548985','-1.388371,54.914561,576.3408698','-2.2913401,53.4630589,598.4888042','-0.0166037,51.5387095,627.3736288','-2.96083,53.4308294,598.9771615'];

final List<String> _stadiumOceania= <String>['Melbourne Cricekt Ground/Melbourne, Australia','Stadium Australia/Sydney, Australia','Perth Stadium/Perth, Australia','Docklands Stadium/Melbourne, Australia','Adelaide Oval/Adelaide, Australia','Lang Park/Brisbane, Australia','Kardinia Park/Geelong, Australia','Hunter Stadium/Newcastle, Australia','Melbourne Rectangular Stadium/Melbourne, Australia','Bankwest Stadium/Parramatta, Australia','Robina Stadium/Gold Coast, Australia','Central Coast Stadium/Gosford, Australia','Perth Oval/Perth, Australia','Hindmarsh Stadium/Adelaide, Australia','Eden Park/ Auckland, New Zeland','Sky Stadium/Pipitea, New Zeland'];
final List<String> _coordOceania= <String>['144.9834493,-37.8199669,813.5890022','151.0634135,-33.8471156,859.6587408','115.8890146,-31.9511708,880.1933939','144.9475055,-37.8165647,813.6301444','138.5961146,-34.9155163,847.6659071','153.0095312,-27.464845,924.7970448','144.3545702,-38.1579763,809.4875393','151.7258453,-32.9178734,869.8442163','144.983782,-37.825132,813.5265362','151.0012237,-33.8081529,860.0904306','153.3787955,-28.066797,919.1488474','151.3377366,-33.4281735,864.2793168','115.8698613,-31.9457767,880.2504263','138.5688783,-34.9073204,847.7590394','174.7447562,-36.8749538,824.9080091','174.7859154,-41.2729218,770.422862'];

final List<String> _stadiumAsia= <String>['Seoul Olympic Stadium/ Seoul, South Korea','Sardar Patel Stadium/ Ahmedabad, India','Bukit Jalil National Stadium/ Kuala Lumpur, Malaysia','Salt Lake Stadium/ Kolkata, India','Shah Alam Stadium/ Shah Alam, Malaysia','New National Stadium/ Tokyo, Japan','Guangdong Olympic Stadium/ Guangzhou, China','Beijing National Stadium/ Beijing, China','Hangzhou Sports Park Stadium/ Hangzhou, China','Palaran Stadium/ Samarinda, Indonesia','Gelora Bung Karno Main Stadium/ Jakarta, Indonesia','Seoul World Cup Stadium/ Seoul, South Korea','International Stadium Yokohama/ Yokohama, Japan','Workers Stadium/ Beijing, China','Saitama Stadium 2002/ Saitama, Japan','Shenyang Olympic Sports Centre Stadium/ Shenyang, China'];
final List<String> _coordAsia= <String>['127.0727661,37.5158554,817.2554327','72.5643479,23.0421763,962.9489775','101.69134,3.0546349,1054.86146','88.4090443,22.5690538,966.6726261','101.5454093,3.0824395,1054.830733','139.7145468,35.6778952,838.9271391','113.4031961,23.1375063,544.6531423','116.390343,39.990029,786.7837249','120.225183,30.2314094,822.207166','117.1315495,-0.5863952,1056.480364','106.8017969,-6.2185701,1049.584634','126.8972774,37.5682588,816.6252554','139.6063937,35.5099461,840.8650564','116.4408471,39.9300533,407.5654298','139.7175976,35.9031054,836.3172279','123.458851,41.739441,764.3816812'];

List<List> footStadiums = [_coordAmerica,_coordAfrica,_coordEurope,_coordOceania,_coordAsia];

var _controllCont = "";
var _controllLenth;
var _controllTitle;
var _controllSubTitle;

class _futlistState extends State<futlist> {

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    _controllCont = widget.continent;

    if(_controllCont != null) {
      switch(_controllCont)
      {
        case 'America':
          {
            _controllLenth = _coordAmerica.length;
            _controllTitle = _stadiumAmerica;
            _controllSubTitle = _coordAmerica;
          }
          break;
        case 'Africa':
          {
            _controllLenth = _coordAfrica.length;
            _controllTitle = _stadiumAfrica;
            _controllSubTitle = _coordAfrica;
          }
          break;
        case 'Europe':
          {
            _controllLenth = _coordEurope.length;
            _controllTitle = _stadiumEurope;
            _controllSubTitle = _coordEurope;
          }
          break;
        case 'Oceania':
          {
            _controllLenth = _coordOceania.length;
            _controllTitle = _stadiumOceania;
            _controllSubTitle = _coordOceania;
          }
          break;
        case 'Asia':
          {
            _controllLenth = _coordAsia.length;
            _controllTitle = _stadiumAsia;
            _controllSubTitle = _coordAsia;
          }
          break;
      }
    }

    if(_controllCont == "")
    {
      return Container(
        color: Colors.white,
        child: Column(
          children: <Widget>[
            Container(
              color: Colors.green,
              height: size.height*.1,
              width: size.width*.95,
              child: Card(
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                  child: Center(
                    child: Text("Select a Continent", textAlign: TextAlign.center,style: TextStyle(
                        color: Colors.black87,fontSize: 22,fontWeight: FontWeight.w700),
                    ),
                  )
              ),
            ),
          ],
        ),
      );
    }
    else
    {
      return Container(
        color: Colors.green,
        child: Container(
          child: ListView.builder(
              padding: const EdgeInsets.all(5),
              itemCount:_controllLenth,
              itemBuilder: (BuildContext context, int index){
                var te = index +1;
                return Card(
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.green,
                      child: Text("$te"),
                      foregroundColor: Colors.white,
                    ),
                    title: Text(_controllTitle[index]),
                    subtitle: Text(_controllSubTitle[index]),
                  ),
                );
              }
          ),
        ),
      );
    }
  }
}

