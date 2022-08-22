import 'package:flutter/material.dart';

class RaccingPage extends StatefulWidget {
  final String continent;
  const RaccingPage(this.continent);
  @override
  _RaccingPageState createState() => _RaccingPageState();
}

final List<String> _raccingAmerica = <String>['Homestead, Florida','Sonoma, California','Austin, Texas','Erda, Utah','Portland, Oregon','Long Pond, Pennsilvània','Palmer, Mssachusetts','Avondale, Arizona','Bowling Green, Kentucky','West Allis, Wisconsin','Concord, North California','Kansas City, Kansas','South Haven, Michigan','Brainerd, Minnesota','Bristol, Tennessee','Hampton, Georgia'];
final List<String> _coordRaccingAmerica = <String>['-80.4111147,25.4547977,431.651452','-122.455413,38.161375,894.8148899','-97.6358511,30.1345808,898.944202','-112.373439,40.5848466,779.2443632','-122.68813,45.5930851,712.7033783','-75.511279,41.0543442,773.2366757','-72.2460134,42.2334253,757.9326331','-112.311173,33.3749503,864.86298','-86.3729674,36.9970956,823.4574136','-88.0138133,43.0209159,747.5425311','-80.686551,35.351547,842.6861711','-94.8318405,39.1160922,797.7126998','-86.139936,42.406696,755.6579639','-94.2849789,46.4175532,701.2559003','-82.2569667,36.515694,829.153195','-84.3163125,33.3859803,864.7420835'];

final List<String> _raccingAfrica = <String>['Wolfelea AH','Pietermaritzburg','Brakpan','Port Shepstone','Delmas','Lichtenburg','Odendaalsurs','Midrand','Route de Thiès, Sindia','Pretòria','Marràqueix','Port Elisabeth','East London','Bloubergstrand, Cape Town','Port Elisabeth','Camperdown'];
final List<String> _coordRaccingAfrica = <String>['27.6291029,-26.0564027,2.079.63','30.4251337,-29.6196542,904.0905105','28.3496982,-26.2072034,936.2507921','30.4249156,-30.7706051,892.4840126','28.755855,-26.075748,937.4206474','26.187458,-26.1509117,936.7523842','26.7112522,-27.9045918,920.68133','28.0759741,-25.997321,938.1161063','-17.0229409,14.599997,1.018.47','28.1100732,-25.8101221,939.7685954','-7.9822639,31.5806507,884.0923502','25.645995,-33.810406,860.0654781','27.8703631,-33.0493094,868.4175662','18.5301713,-33.8228891,859.9272071','25.5575288,-34.0126457,857.820266','30.5244086,-29.6963558,903.3287209'];

final List<String> _raccingEurope = <String>['Alcarràs, Spain','Montemló, Spain','Spielberg, Austria','Stavelot, Belgium','Hockenheim, Germany','Monza, Italy','Le Mans, France','Zandvoort, Netherlands','Mogyoród, Hungary','Scarperia e San Piero, Italy','Chichester, United Kingdom','Nürburg, Germany','Alcabideche, Portugal','Lo Castelet, France','Towcester, United Kingdom','Misano Adriatico, Italy'];
final List<String> _coordRaccingEurope = <String>['0.4023381,41.6182964,765.9550702','2.2571491,41.5682267,766.6044141','14.7651864,47.220181,689.9866211','5.97205,50.4369118,643.6501822','8.5709249,49.3298956,659.8010414','9.2848,45.621799,712.3069208','0.2078705,47.9560052,679.5497077','4.5442128,52.3877081,614.6997162','19.2506106,47.5817111,684.8711423','11.3713239,43.9965935,734.4871231','-0.751715,50.859426,637.431826','6.9426625,50.3340981,645.1588832','-9.3941956,38.7505686,802.2306107','5.7927315,43.2517169,744.4722638','-1.0146634,52.0733006,619.406301','12.6859581,43.961376,734.961817'];

final List<String> _raccingOceania = <String>['Adelaide, Australia','Virginia, Australia','Kurwongbah, Australia','Perth, Australia','Hidden Valley, Austràlia','Mallala, Australia','Albert Park, Australia','Morgan Park, Australia','Surfers Paradise, Australia','Neerabup, Australia','Ventor, Australia','Mount Panorama, Australia','Springvale, Australia','Calder Park, Australia','Willowbank, Australia','Eastern Creek, Austràlia'];
final List<String> _coordRaccingOceania = <String>['138.6196917,-34.9309643,847.4903199','138.5651708,-34.6993367,850.1165874','152.9637649,-27.2276608,926.9932073','147.2499587,-41.6616444,765.3924514','130.9069894,-12.4487367,1028.794348','138.5041988,-34.4164557,853.3050525','144.9690202,-37.8500625,813.2249394','152.0311678,-28.2623035,917.2914988','153.4274036,-27.9863581,919.9097828','115.7892504,-31.6644598,883.2137418','145.2316062,-38.4969348,805.3469117','149.5577778,-33.4394444,864.1556194','145.1658348,-37.9507293,812.0055885','144.760098,-37.6713194,1789.479928','152.6536812,-27.6910665,922.6868907','150.8707252,-33.8063171,860.1107605'];

final List<String> _raccingAsia = <String>['Jinjiang District, Chengdu','Chaoyang, Beijing','Mevalurkuppam, India','Motegi, Japan','Hita, Oita, Japan','Oyama, Japan','Rosario, Philippines','Sendai, Japan','Marina Bay, Singapur','Doha, Qatar','Abu Dhabi','Yongin, South Korea','Buriram Buri Ram, Thailand','Pasir Gudang, Malaysia','Wangchan, Thailand','Sepang, Malaysia'];
final List<String> _coordRaccingAsia = <String>['104.121501,30.585175,894.3792123','116.5631629,40.018022,786.43073','79.987432,12.99972,1026.301382','140.2253966,36.531954,828.9617548','130.97351,33.039077,868.5287989','138.9272935,35.3722685,842.4483015','121.2776576,13.8214685,1022.388652','140.6299259,38.2916815,807.8575263','103.8639097,1.2914319,1056.241794','51.4528912,25.4862831,942.6021872','54.6055067,24.4699413,951.2872923','127.207591,37.2960689,819.8911501','103.0849254,14.9579714,1016.595384','103.9077126,1.4805879,1056.147316','99.706249,12.942246,1026.566332','101.7317775,2.7594144,1055.170563'];

List<List> raccingCircuits =[_coordRaccingAmerica,_coordRaccingAfrica,_coordRaccingEurope,_coordRaccingOceania,_coordRaccingAsia];

var _controllCont = "";
var _controllLenth;
var _controllTitle;
var _controllSubTitle;

class _RaccingPageState extends State<RaccingPage> {
  @override
  Widget build(BuildContext context) {
    var size = MediaQuery.of(context).size;
    _controllCont = widget.continent;

    if(_controllCont != null) {
      switch(_controllCont)
      {
        case 'America':
          {
            _controllLenth = _coordRaccingAmerica.length;
            _controllTitle = _raccingAmerica;
            _controllSubTitle = _coordRaccingAmerica;
          }
          break;
        case 'Africa':
          {
            _controllLenth = _coordRaccingAfrica.length;
            _controllTitle = _raccingAfrica;
            _controllSubTitle = _coordRaccingAfrica;
          }
          break;
        case 'Europe':
          {
            _controllLenth = _coordRaccingEurope.length;
            _controllTitle = _raccingEurope;
            _controllSubTitle = _coordRaccingEurope;
          }
          break;
        case 'Oceania':
          {
            _controllLenth = _coordRaccingOceania.length;
            _controllTitle = _raccingOceania;
            _controllSubTitle = _coordRaccingOceania;
          }
          break;
        case 'Asia':
          {
            _controllLenth = _coordRaccingAsia.length;
            _controllTitle = _raccingAsia;
            _controllSubTitle = _coordRaccingAsia;
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
              color: Colors.blue,
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
        color: Colors.blue,
        child: Container(
          child: ListView.builder(
              padding: const EdgeInsets.all(5),
              itemCount:_controllLenth,
              itemBuilder: (BuildContext context, int index){
                var te = index +1;
                return Card(
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.blue,
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
