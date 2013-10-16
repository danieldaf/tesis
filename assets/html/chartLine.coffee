# http://bost.ocks.org/mike/chart/
	
lineChart = () ->

	result = () ->

	result.updateSeries = (datosSeries) ->
		paths = _svgData.selectAll("path")
			.data(datosSeries, (d) -> d.name)
		
		#update
		
		#append
		paths.enter().append("path")
			.attr("stroke", (d) -> d.color)
			.attr("stroke-width", 1)
			.attr("fill", "none")
			#.style("stroke-opacity", "0")
			#.transition()
			#	.duration(500)
			#	.delay((d,i) -> i*100)
				.style("stroke-opacity", "1")
		
		#update + append
		paths.attr("d", (d) -> _line(d.data))
		
		#remove
		paths.exit()
			#.transition()
			#	.duration(500)
			#	.style("stroke-opacity", "0")
				.remove()

		#leyenda
		svgColors = _svgLegend.selectAll("rect")
			.data(datosSeries, (d) -> d.name)
		svgColors.transition()
			.delay((d,i) -> i*100)
			.duration(500)
			.attr("y", (d, i) -> 10+(20+5)*i)
		svgColors.enter().append("rect")
			.attr("x", "10")
			.attr("y", (d, i) -> 10+(20+5)*i)
			.attr("width", "20")
			.attr("height", "20")
			.attr("stroke", "#000")
			.attr("stroke-width", "1")
			#.attr("stroke-opacity", "0")
			.attr("fill", (d) -> d.color)
			#.attr("fill-opacity", "0")
			#.transition()
			#	.delay((d,i) -> i*100)
			#	.duration(1500)
				.attr("stroke-opacity", "1")
				.attr("fill-opacity", "1")
		svgColors.exit()
			#.transition()
			#	.duration(500)
			#	.attr("stroke-opacity", "0")
			#	.attr("fill-opacity", "0")
				.remove()

		svgTexts = _svgLegend.selectAll("text")
			.data(datosSeries, (d)->d.name)
		svgTexts
		#svgTexts.transition()
		#	.delay((d,i) -> i*100)
		#	.duration(500)
			.attr("x", 10+20+5)
			.attr("y", (d,i) -> 10+(20+5)*i)
		svgTexts.enter().append("text")
			.attr("x", 10+20+5)
			.attr("y", (d,i) -> 10+(20+5)*i)
			.attr("dy", "13px")
			#.attr("stroke-opacity", "0")
			#.attr("fill-opacity", "0")
			.text((d) -> d.name)
			#.transition()
			#	.delay((d,i) -> i*100)
			#	.duration(1500)
				.attr("stroke-opacity", "1")
				.attr("fill-opacity", "1")
		svgTexts.exit()
			#.transition()
			#	.duration(500)
			#	.attr("stroke-opacity", "0")
			#	.attr("fill-opacity", "0")
				.remove()

	width = 640
	height = 480
	
	margins =
		left: 50
		right: 50
		top: 50
		bottom: 50
		
	title = "Grafico"
	labelAxisX = "Eje X"
	labelAxisY = "Eje Y"
		
	domainX = []
	domainY = []
		
	titleLegend = "Leyenda"
	widthLegend = 200
	
	idTagInsert = "chart"
	
	result.idTagInsert = (value) ->
		if !arguments.length
			idTagInsert
		else
			idTagInsert = value
			result

	result.width = (value) ->
		if !arguments.length
			width
		else
			width = value
			result
			
	result.height = (value) ->
		if !arguments.length
			height
		else
			height = value
			result
	
	result.margins = (value) ->
		if (!arguments.length)
			margins
		else
			margins = value
			result

	result.title = (value) ->
		if (!arguments.length)
			title
		else
			title = value	
			result
			
	result.labelAxisX = (value) ->
		if (!arguments.length)
			labelAxisX
		else
			labelAxisX = value	
			result
 
	result.labelAxisY = (value) ->
		if (!arguments.length)
			labelAxisY
		else
			labelAxisY = value	
			result

	result.titleLegend = (value) ->
		if (!arguments.length)
			titleLegend
		else
			titleLegend = value	
			result

	result.widthLegend = (value) ->
		if (!arguments.length)
			widthLegend
		else
			widthLegend = value	
			result
		
	result.domainX = (value) ->
		if (!arguments.length)
			domainX
		else
			domainX = value	
			result
			
	result.domainY = (value) ->
		if (!arguments.length)
			domainY
		else
			domainY = value	
			result

	_svg = () ->
	_x = () ->
	_y = () ->
	_fondoH = () ->
	_fondoV = () ->
	_svgLegend = () ->
	_svgData = () ->
	_line = () -> 
	
	result.build = () ->
		_x  = d3.scale.linear().domain([domainX[0], domainX[domainX.length-1]]).range [0, width]
		_y  = d3.scale.linear().domain([domainY[0], domainY[domainY.length-1]]).range [height, 0]
		
		_line = d3.svg.line()
			.interpolate("linear")
			.x((d,i) -> _x(domainX[i]))
			.y(_y)

		_svg = d3.select("div#"+idTagInsert)
			.attr("style", "display:block")
			.append("svg")
			.attr("id", idTagInsert)
			.attr("width", width+margins.left+margins.right+widthLegend)
			.attr("height", height+margins.top+margins.bottom)
		
		ticksCountX = domainX.length
		ticksCountY = domainY.length
		
		relacionTicksX = Math.round(width / ticksCountX)
		relacionTicksY = Math.round(height/ ticksCountY)
		
		ticksSugeridosX = Math.round(width / 30)
		ticksSugeridosY = Math.round(height / 20)
		
		ticksCountX = ticksSugeridosX if relacionTicksX < 30
		ticksCountY = ticksSugeridosY if relacionTicksY < 20
	
		xAxis = d3.svg.axis()
			.scale(_x)
			.tickValues(_x.ticks(ticksCountX))
			.orient("bottom")
	
		yAxis = d3.svg.axis()
			.scale(_y)
			.tickValues(_y.ticks(ticksCountY))
			.orient("left")

		_svg.append("g")
			.attr("transform", "translate("+margins.left+", "+(height+margins.top)+")")
			.attr("class", "axis")
		  	.call(xAxis)
		  	.select("text")
		  		.attr("x", width/2)
		  		.attr("text-anchor", "middle")
		  		.attr("dy", "25px")
		  		.text(labelAxisX)
	
		_svg.append("g")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			.attr("class", "axis")
		  	.call(yAxis)
		  	.select("text")
		  		.attr("transform", "rotate(-90)")
		  		.attr("dy", "-25px")
		  		.attr("text-anchor", "middle")
		  		.attr("x", height/2)
		  		.text(labelAxisY)
  	
		_fondoH = _svg.append("g")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			.attr("class", "fondo")
		
		_fondoH.selectAll("line")
			.data(domainY[1..domainY.length-2])
			.enter().append("line")
				.attr("x1", _x(domainX[0]))
				.attr("y1", _y)
				.attr("x2", _x(domainX[domainX.length-1]))
				.attr("y2", _y)
	
		_fondoV = _svg.append("g")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			.attr("class", "fondo")
		
		_fondoV.selectAll("line")
			.data(domainX[1..domainX.length-2])
			.enter().append("line")
				.attr("x1", _x)
				.attr("y1", _y(domainY[0]))
				.attr("x2", _x)
				.attr("y2", _y(domainY[domainY.length-1]))
				
		_svg.append("g")
			.attr("transform", "translate("+(margins.left+width/3)+", 15)")
			.attr("class", "title")
			.append("text").text(title)

		_svgLegend = _svg.append("g")
			.attr("transform", "translate("+(margins.left+width+25)+", "+margins.top+")")
		_svgLegend.append("text").text(titleLegend)
		_svgLegend = _svg.append("g")
			.attr("id", "leyenda")
			.attr("transform", "translate("+(margins.left+width+25)+", "+margins.top+")")
	
		_svgData = _svg.append("g")
			.attr("id", "series")
			.attr("transform", "translate("+margins.left+", "+margins.top+")")
			
		result
		
	result
