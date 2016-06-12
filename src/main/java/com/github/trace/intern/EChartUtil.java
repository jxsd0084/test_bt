package com.github.trace.intern;

import com.alibaba.fastjson.JSON;
import com.github.trace.TraceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EChart图表工具类
 * Created by jiangwj on 2016/2/18.
 */
public class EChartUtil {

	public static void main( String[] args ) {

		String   rpcId = "0.1";
		String[] ids   = rpcId.split( "\\." );
		System.out.println( ids[ 0 ] );
	}

	public static EChartTreeItemStyle getItemStyle( String color ) {

		EChartTreeItemStyle     itemStyle     = new EChartTreeItemStyle();
		EChartTreeItemStyleData itemStyleData = new EChartTreeItemStyleData();
		itemStyleData.setBorderWidth( 0 );
		itemStyleData.setColor( color );
		EChartTreeItemStyleLabel label = new EChartTreeItemStyleLabel();
		label.setShow( true );
		itemStyleData.setLabel( label );
		itemStyle.setNormal( itemStyleData );
		return itemStyle;
	}

	public static String toTreeMapData( List< TraceContext > rpc ) {

		String treeData = "";

		List< EChartTreeNode > rootList = new ArrayList<>();

		EChartTreeNode rootNode = new EChartTreeNode();
		rootNode.setName( "起点" );
		rootNode.setItemStyle( getItemStyle( EnumEChartItemStyleColor.LIGHT.toString() ) );

		Map< String, EChartTreeNode > nodeMap = new HashMap<>();

		for ( TraceContext c : rpc ) {
			String rpcId = c.getRpcId();
			String n     = rpcId.split( "\\." )[ 0 ];
			String root  = n + ".";
			if ( !nodeMap.containsKey( root ) ) {
				EChartTreeNode node = new EChartTreeNode();
				if ( root.equals( "0." ) ) {
					node.setName( "第" + n + "次Rpc请求" );
				} else {
					node.setName( "第" + n + "次ajax请求" );
				}
				node.setItemStyle( getItemStyle( EnumEChartItemStyleColor.LIGHT.toString() ) );
				nodeMap.put( root, node );
				if ( rootNode.getChildren() == null ) {
					List< EChartTreeNode > childs = new ArrayList<>();
					childs.add( node );
					rootNode.setChildren( childs );
				} else {
					rootNode.getChildren().add( node );
				}
			}
		}

		for ( TraceContext c : rpc ) {
			String         rpcId  = c.getRpcId();
			String         pid    = rpcId.substring( 0, rpcId.length() - 1 );
			EChartTreeNode ecNode = new EChartTreeNode();
			ecNode.setName( c.getIface() + "." + c.getMethod() );
			ecNode.setValue( String.valueOf( c.getCost() ) );
			if ( c.getCost() > 200 ) {
				ecNode.setItemStyle( getItemStyle( EnumEChartItemStyleColor.SERIOUS.toString() ) );
			} else if ( c.getCost() > 100 && c.getCost() <= 200 ) {
				ecNode.setItemStyle( getItemStyle( EnumEChartItemStyleColor.HEAVY.toString() ) );
			} else if ( c.getCost() > 20 && c.getCost() <= 100 ) {
				ecNode.setItemStyle( getItemStyle( EnumEChartItemStyleColor.MIDDLE.toString() ) );
			} else if ( c.getCost() <= 20 ) {
				ecNode.setItemStyle( getItemStyle( EnumEChartItemStyleColor.LIGHT.toString() ) );
			}

			if ( nodeMap.containsKey( pid ) ) {
				EChartTreeNode         n      = nodeMap.get( pid );
				List< EChartTreeNode > childs = n.getChildren();
				if ( childs == null ) {
					childs = new ArrayList<>();
					n.setChildren( childs );
				}
				childs.add( ecNode );
			}
			nodeMap.put( rpcId, ecNode );
		}


		rootList.add( rootNode );

		treeData = JSON.toJSONString( rootList );

		return treeData;
	}

	static enum EnumEChartItemStyleColor {
		LIGHT( "#FF88C2" ), MIDDLE( "#FFA488" ), HEAVY( "#FF7744 " ), SERIOUS( "#FF3333" );

		String value;

		EnumEChartItemStyleColor( String value ) {

			this.value = value;
		}

		public String toString() {

			return value;
		}
	}

	static class EChartTreeNode {

		public String getName() {

			return name;
		}

		public void setName( String name ) {

			this.name = name;
		}

		public String getValue() {

			return value;
		}

		public void setValue( String value ) {

			this.value = value;
		}

		public String getSymbolSize() {

			return symbolSize;
		}

		public void setSymbolSize( String symbolSize ) {

			this.symbolSize = symbolSize;
		}

		public String getSymbol() {

			return symbol;
		}

		public void setSymbol( String symbol ) {

			this.symbol = symbol;
		}

		public EChartTreeItemStyle getItemStyle() {

			return itemStyle;
		}

		public void setItemStyle( EChartTreeItemStyle itemStyle ) {

			this.itemStyle = itemStyle;
		}

		public List< EChartTreeNode > getChildren() {

			return children;
		}

		public void setChildren( List< EChartTreeNode > children ) {

			this.children = children;
		}

		String                 name;
		String                 value;
		String                 symbolSize;
		String                 symbol;
		EChartTreeItemStyle    itemStyle;
		List< EChartTreeNode > children;

	}

	static class EChartTreeItemStyle {

		public EChartTreeItemStyleData getNormal() {

			return normal;
		}

		public void setNormal( EChartTreeItemStyleData normal ) {

			this.normal = normal;
		}

		public EChartTreeItemStyleData getEmphasis() {

			return emphasis;
		}

		public void setEmphasis( EChartTreeItemStyleData emphasis ) {

			this.emphasis = emphasis;
		}

		EChartTreeItemStyleData normal;
		EChartTreeItemStyleData emphasis;
	}

	static class EChartTreeItemStyleLabel {

		public boolean isShow() {

			return show;
		}

		public void setShow( boolean show ) {

			this.show = show;
		}

		public String getPosition() {

			return position;
		}

		public void setPosition( String position ) {

			this.position = position;
		}

		public String getFormatter() {

			return formatter;
		}

		public void setFormatter( String formatter ) {

			this.formatter = formatter;
		}

		boolean show;
		String  position;
		String  formatter;
	}

	static class EChartTreeItemStyleData {

		public String getColor() {

			return color;
		}

		public void setColor( String color ) {

			this.color = color;
		}

		public int getBorderWidth() {

			return borderWidth;
		}

		public void setBorderWidth( int borderWidth ) {

			this.borderWidth = borderWidth;
		}

		public String getBorderColor() {

			return borderColor;
		}

		public void setBorderColor( String borderColor ) {

			this.borderColor = borderColor;
		}

		public String getBrushType() {

			return brushType;
		}

		public void setBrushType( String brushType ) {

			this.brushType = brushType;
		}

		public EChartTreeItemStyleLabel getLabel() {

			return label;
		}

		public void setLabel( EChartTreeItemStyleLabel label ) {

			this.label = label;
		}

		String                   color;
		int                      borderWidth;
		String                   borderColor;
		String                   brushType;
		EChartTreeItemStyleLabel label;
	}

}
