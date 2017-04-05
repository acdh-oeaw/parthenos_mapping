<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    
    <xsl:template match="/">
        <html>
            <head>
                <style type="text/css">
                    table, td {border:1px solid grey;}
              tr.domain {font-weight:bold;}
                </style>
            </head>
            <h1>3M Mapping: <xsl:value-of select="/x3ml/info/title"></xsl:value-of></h1>
            <p>source file: <strong><xsl:value-of select="replace(document-uri(/),'^.*/([^/]*)','$1')" /></strong></p>
            <xsl:apply-templates ></xsl:apply-templates>
        </html>
    </xsl:template>
    
    <xsl:template match="mappings">
        <table>
            <xsl:apply-templates></xsl:apply-templates>
        </table>       
    </xsl:template>
    
    <xsl:template match="mapping">
        
        <xsl:apply-templates />
                
    </xsl:template>
    
    <xsl:template match="domain">
        <tr class="domain">
            <td>D</td>
            <td><xsl:apply-templates select="source_node"></xsl:apply-templates></td>
            <td><xsl:apply-templates select="target_node"></xsl:apply-templates></td>
            <td><xsl:apply-templates select="target_node" mode="generators"></xsl:apply-templates></td>
        </tr>
        
    </xsl:template>
    
    <xsl:template match="link">
        <tr>
            <td>R</td>
            <td><xsl:apply-templates select="*/source_relation" />
                <xsl:if test="not(*/source_relation/relation/text() = */source_node/text())" >
                    <br/>
                <xsl:apply-templates select="*/source_node"></xsl:apply-templates>
                </xsl:if>
            </td>            
            <td><xsl:apply-templates select="*/(target_relation, target_node)"></xsl:apply-templates></td>
            <td><xsl:apply-templates select="*/target_node" mode="generators"></xsl:apply-templates></td>
        </tr>
        
    </xsl:template>
    
    <xsl:template match="source_node">        
            <xsl:value-of select="."/>       
    </xsl:template>
    
    <xsl:template match="target_node/entity">
            <xsl:value-of select="type"/>
    </xsl:template>
    

<xsl:template match="range/source_node">        
            <xsl:value-of select="."/>                
    </xsl:template>
    
    <xsl:template match="range/target_node">
                   <span class="link"> → </span> <xsl:value-of select="entity/type" />
    </xsl:template>

    
    <xsl:template match="source_relation">
            <span class="link"> → </span> <xsl:value-of select="relation"/>
                
    </xsl:template>
    
    <xsl:template match="target_relation">        
           <xsl:apply-templates />            
                
    </xsl:template>

    <xsl:template match="target_relation/*">
                   <span class="link"> → </span> <xsl:value-of select="." />
    </xsl:template>

    <xsl:template match="target_node" mode="generators">
                <xsl:apply-templates  mode="generators"></xsl:apply-templates>
    </xsl:template>

    <xsl:template match="type" mode="generators" />
    
    <xsl:template match="instance_info" mode="generators">
       <xsl:value-of select="*/local-name()"></xsl:value-of>
       <xsl:text>(</xsl:text><xsl:value-of select="*"></xsl:value-of><xsl:text>)</xsl:text><br/>
    </xsl:template>


    <xsl:template match="instance_generator|label_generator" mode="generators">
            <xsl:value-of select="@name"></xsl:value-of>
<xsl:text>(</xsl:text><xsl:apply-templates mode="generators"></xsl:apply-templates><xsl:text>)</xsl:text><br/>
    </xsl:template>

    <xsl:template match="arg[@type='constant']" mode="generators">
        <span class="arg.constant"><xsl:text>'</xsl:text>
            <xsl:value-of select="."></xsl:value-of>
            <xsl:text>'</xsl:text>
          </span>
    </xsl:template>
    
    <xsl:template match="arg[@type='xpath']" mode="generators">
        <span class="arg.xpath">
            <xsl:value-of select="."></xsl:value-of>            
          </span>
    </xsl:template>

    <xsl:template match="additional" mode="generators">
           <span>+ </span>
            <xsl:apply-templates mode="generators" />
    </xsl:template>

    <xsl:template match="additional/relationship" mode="generators">
        <span class="link"> → </span>  <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="additional/entity" mode="generators">
        <span class="link"> → </span>  <xsl:value-of select="type"/>
        <xsl:apply-templates mode="generators" />
        
    </xsl:template>
    
    <xsl:template match="text()" />
</xsl:stylesheet>