package Calculations;

import georegression.struct.point.Point2D_I32;

public class IntersectionLines {

	  public static Point2D_I32 intersectLines(Point2D_I32 point1, Point2D_I32 point2, Point2D_I32 point3, Point2D_I32 point4) {
		 
		  int x1 = point1.x;
		  int y1 = point1.y;		  

		  int x2 = point2.x;
		  int y2 = point2.y;
		  
		  int x3 = point3.x;
		  int y3 = point3.y;
		  
		  int x4 = point4.x;
		  int y4 = point4.y;
		  
		  double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		  
		  if (denom == 0.0) { // Lines are parallel.
		     return null;
		  }
		  
		  double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		  double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
		  
		  if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
		        // Get the intersection point.
		        return new Point2D_I32((int) (x1 + ua*(x2 - x1)), (int) (y1 + ua*(y2 - y1)));
		  }

		  return null;
		  
	}
	  
	//Calculates intersection point of two lines
	 public static Point2D_I32 findIntersection(Point2D_I32 p1,Point2D_I32 p2,  
			 Point2D_I32 p3,Point2D_I32 p4) {  
		 
			  double xD1,yD1,xD2,yD2,xD3,yD3;  
			  double dot,deg,len1,len2;  
			  double segmentLen1,segmentLen2;  
			  double ua,ub,div;  
			      
			  // calculate differences  
			  xD1=p2.x-p1.x;  
			  xD2=p4.x-p3.x;  
			  yD1=p2.y-p1.y;  
			  yD2=p4.y-p3.y;  
			  xD3=p1.x-p3.x;  
			  yD3=p1.y-p3.y;    
			    
			  // calculate the lengths of the two lines  
			  len1 = Math.sqrt(xD1*xD1+yD1*yD1);  
			  len2 = Math.sqrt(xD2*xD2+yD2*yD2);  
			  
			  // calculate angle between the two lines.  
			  dot = (xD1*xD2+yD1*yD2); // dot product  
			  deg = dot/(len1*len2);  
			    
			  // if abs(angle)==1 then the lines are parallell,  
			  // so no intersection is possible  
			  if(Math.abs(deg)==1) return null;  
			  
			  // find intersection Pt between two lines    
			  Point2D_I32 pt=new Point2D_I32(0,0);  
			  
			  div=yD2*xD1-xD2*yD1;  
			  ua=(xD2*yD3-yD2*xD3)/div;  
			  ub=(xD1*yD3-yD1*xD3)/div;  
			  
			  pt.x=(int) (p1.x+ua*xD1);  
			  pt.y=(int) (p1.y+ua*yD1);  
			    
			  // calculate the combined length of the two segments  
			  // between Pt-p1 and Pt-p2  
			  xD1=pt.x-p1.x;  
			  xD2=pt.x-p2.x;  
			  yD1=pt.y-p1.y;  
			  yD2=pt.y-p2.y;  
			  segmentLen1 = Math.sqrt(xD1*xD1+yD1*yD1) + Math.sqrt(xD2*xD2+yD2*yD2);  
			    
			  // calculate the combined length of the two segments  
			  // between Pt-p3 and Pt-p4  
			  xD1=pt.x-p3.x;  
			  xD2=pt.x-p4.x;  
			  yD1=pt.y-p3.y;  
			  yD2=pt.y-p4.y;  
			  
			  segmentLen2 = Math.sqrt(xD1*xD1+yD1*yD1) + Math.sqrt(xD2*xD2+yD2*yD2);  
			  
			  // if the lengths of both sets of segments are the same as  
			  // the lenghts of the two lines the point is actually   
			  // on the line segment.  
			  
			  // if the point isn't on the line, return null  
			  if(Math.abs(len1-segmentLen1)>0.01 || Math.abs(len2-segmentLen2)>0.01)   
			    return null;  
			  
			  // return the valid intersection  
			  return pt;  
		} 
	
}
