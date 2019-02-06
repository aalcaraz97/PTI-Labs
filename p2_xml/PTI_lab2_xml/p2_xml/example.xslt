<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/carrental">
		<html>
			<head><title>RENTALS</title></head>
			<body>
				<xsl:apply-templates select="rental"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="rental">
		<table border="0">
			<h1>MAKE=<xsl:value-of select="make"/></h1><br />
			<h2>MODEL=<xsl:value-of select="model"/></h2><br />
			<tr><td>Number of days</td><td><xsl:value-of select="nofdays"/></td></tr>
			<tr><td>Number of units</td><td><xsl:value-of select="nofunits"/></td></tr>
			<tr><td>Discount</td><td><xsl:value-of select="discount"/></td></tr>
		</table>        
	</xsl:template>
</xsl:stylesheet>
