// MapNavigation.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <GL/glut.h>
#include <gl\glaux.h>
#include <stdio.h>
#include <sstream>
#include <string>

using namespace std;

#pragma comment(lib,"opengl32.lib")
#pragma comment(lib,"glu32.lib")
#pragma comment(lib,"glut32.lib")
#pragma comment(lib,"GLaux.lib")

int win_width=640;
int win_height=810;

int g_map_width=20;
int g_map_height=20;

int g_tile_wide=10;
int g_tile_high=10;

int g_XPos=0;
int g_YPos=0;

int tile_size=64;
int y_offset=650;
//holds the tiles of the map
GLuint map_tiles[400];
GLuint border;
GLuint LoadTexture(char *FileName ) 
{ 
	FILE *File=NULL;          
	GLuint ID;
	File=fopen(FileName,"r");     

	if (File)            
	{ 
		fclose(File);    
	} 

	AUX_RGBImageRec *TextureImage=new(AUX_RGBImageRec);       
	memset(TextureImage,0,sizeof(void *)*1);             
	TextureImage = auxDIBImageLoad(FileName); 

	glGenTextures(1, &ID);       
	glBindTexture(GL_TEXTURE_2D,ID); 

	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST); 
	glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST); 
	glTexImage2D(GL_TEXTURE_2D, 0, 3, TextureImage->sizeX, TextureImage->sizeY, 0, GL_RGB, GL_UNSIGNED_BYTE, TextureImage->data); 

	free(TextureImage->data);        
	free(TextureImage);   
	
	return ID;
}

void LoadAllSlices()
{
	
	for(int i=0;i<g_map_height*g_map_width;i++)
	{
		std::stringstream str_number;
		str_number<<i+1;
		string fileName="map_slices\\map_"+str_number.str()+".bmp";
			map_tiles[i]=LoadTexture((char*)fileName.c_str());
		
	}
	border=LoadTexture("border.bmp");
}

void RenderBorder()
{
	glPolygonMode (GL_FRONT_AND_BACK, GL_FILL);
	//top border
	glBindTexture(GL_TEXTURE_2D,  border);
	glBegin(GL_QUADS);
			glTexCoord2f(0.0f,1.0f);
			glVertex2f(0,0);
			glTexCoord2f(1.0f,1.0f);
			glVertex2f(win_width,0);
			glTexCoord2f(1.0f,0.0f);
			glVertex2f(win_width,20);
			glTexCoord2f(0.0f,0.0f);
			glVertex2f(0,20);
	glEnd();
	
	glBegin(GL_QUADS);
			glTexCoord2f(1.0f,0.0f);
			glVertex2f(0,y_offset-10);
			glTexCoord2f(0.0f,0.0f);
			glVertex2f(win_width,y_offset-10);
			glTexCoord2f(0.0f,1.0f);
			glVertex2f(win_width,y_offset+10);
			glTexCoord2f(1.0f,1.0f);
			glVertex2f(0,y_offset+10);
	glEnd();
}

void RenderMapSlices()
{
	glPolygonMode (GL_FRONT_AND_BACK, GL_FILL);
	for(int y=0;y<g_tile_high;y++)
	{
		for(int x=0;x<g_tile_wide;x++)
		{

			glBindTexture(GL_TEXTURE_2D,  map_tiles[(x+g_XPos)+(y+g_YPos)*g_map_width]);

			int pixel_x=x*tile_size;
			int pixel_y=y*tile_size;

			glBegin(GL_QUADS);
				glTexCoord2f(0.0f,1.0f);
				glVertex2f(pixel_x,pixel_y);
				glTexCoord2f(1.0f,1.0f);
				glVertex2f(pixel_x+tile_size,pixel_y);
				glTexCoord2f(1.0f,0.0f);
				glVertex2f(pixel_x+tile_size,pixel_y+tile_size);
				glTexCoord2f(0.0f,0.0f);
				glVertex2f(pixel_x,pixel_y+tile_size);
			glEnd();

		}

	}
}
void RenderMiniMap()
{
	
	int dot_size=8;

	for(int y=y_offset;y<y_offset+g_map_height;y++)
	{
		for(int x=0;x<g_map_width;x++)
		{
			int pixel_x=x*dot_size;
			int pixel_y= y_offset + (y-y_offset)*dot_size;
			glBindTexture(GL_TEXTURE_2D,  map_tiles[x+(y-y_offset)*g_map_width]);
			
			glBegin(GL_QUADS);
				glTexCoord2f(0.0f,1.0f);	
				glVertex2f(pixel_x,pixel_y);
				glTexCoord2f(1.0f,1.0f);
				glVertex2f(pixel_x+dot_size,pixel_y);
				glTexCoord2f(1.0f,0.0f);
				glVertex2f(pixel_x+dot_size,pixel_y+dot_size);
				glTexCoord2f(0.0f,0.0f);
				glVertex2f(pixel_x,pixel_y+dot_size);
			glEnd();

					
		}
	}

		//render the small rectangle to show your pos on the map
	int rect_pixel_x=g_XPos*dot_size;
	int rect_pixel_y=y_offset+g_YPos*dot_size;

	int rect_pixel_width=g_tile_wide*dot_size;
	int rect_pixel_height=g_tile_high*dot_size;
	glPolygonMode (GL_FRONT_AND_BACK, GL_LINE);
	glBegin(GL_QUADS);
				
				glVertex2f(rect_pixel_x,rect_pixel_y);
				
				glVertex2f(rect_pixel_x+rect_pixel_width,rect_pixel_y);
				
				glVertex2f(rect_pixel_x+rect_pixel_width,rect_pixel_y+rect_pixel_height);
				
				glVertex2f(rect_pixel_x,rect_pixel_y+rect_pixel_height);
	glEnd();

	
	
}
void init()
{
	glClearColor (0.0, 0.0, 0.0, 0.0);
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	
	glEnable(GL_TEXTURE_2D);



	LoadAllSlices();

}
void render()
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	
	
	RenderMapSlices();
	RenderMiniMap();

	RenderBorder();

	glutSwapBuffers();
}
void reshape(int w,int h) 
{
	glViewport(0,0,(GLsizei)w,(GLsizei)h);
	
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(0,w,h,0);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}
void MouseMotion(int x,int y)
{

	int mouse_tile_XPos=x/tile_size;
	int mouse_tile_YPos=y/tile_size;

	if(mouse_tile_XPos == g_tile_wide-1)
	{
		g_XPos++;
		if(g_XPos>=g_map_width-g_tile_wide)
			g_XPos=g_map_width-g_tile_wide;
	}
	else if(mouse_tile_XPos ==0)
	{
		g_XPos--;
		if(g_XPos<0)
			g_XPos=0;
	}
	else if(mouse_tile_YPos == g_tile_high-1)
	{
		g_YPos++;
		if(g_YPos >= g_map_height-g_tile_high)
			g_YPos=g_map_height-g_tile_high;
	}
	else if(mouse_tile_YPos == 0)
	{
		g_YPos--;
		if(g_YPos <0)
			g_YPos=0;
	}


}
void timer_func(int n)          
{
		glutPostRedisplay();
        glutTimerFunc(n, timer_func, 0);
}
int main(int argc, char* argv[])
{
	glutInit(&argc, argv);
	glutInitDisplayMode ( GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize (win_width, win_height); 
	glutInitWindowPosition (0, 0);
	glutCreateWindow ("Map navigation");

	init ();

	glutDisplayFunc(render); 
	glutIdleFunc(render);

	glutReshapeFunc(reshape);
    glutPassiveMotionFunc(MouseMotion);
  
	timer_func(1000);

	glutMainLoop();
	return 0;
}
