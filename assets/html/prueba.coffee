seriesLines = [{
	name: "amarillo"
	color: "yellow"
	data: [0,10,30,70,100] }, { 
	name: "azul"
	color: "blue"
	data: [0,30,25,50,90] }, {
	name: "rojo"
	color: "red"
	data: [100,70,5,45,63]}, {
	name: "verde"
	color: "green"
	data: [50,10,97,35,8] }]

seriesBars = [{
	name: "alg01"
	data: [50, 25, 5, 80] }, { 
	name: "alg02", 
	data: [12, 20, 5, 37] }, { 
	name: "alg03", 
	data: [27, 20, 50, 97] }]

myChart1 = lineChart()
	.idTagInsert('chart2')
	.title('Grafico de Prueba 1')
	.domainX([0,10,20,30,40])
	.domainY([0,10,20,30,40,50,60,70,80,90,100])
	.width(350)
	.height(250)
	.build()

myChart2 = barHChart()
	.idTagInsert('chart1')
	.title('Grafico de Prueba 2')
	.metricUnit('[ms]')
	.domainX([0,10,20,30,40,50,60,70,80,90,100])
	.semanticColors(["sangre", "clorofila", "oro", "agua"])
	.colors(["red", "green", "yellow", "blue"])
	.width(600)
	.height(300)
	.build()

myChart1.updateSeries(seriesLines)
myChart2.updateSeries(seriesBars[0..2])
