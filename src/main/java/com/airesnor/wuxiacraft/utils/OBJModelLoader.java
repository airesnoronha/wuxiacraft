package com.airesnor.wuxiacraft.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;

public class OBJModelLoader {

	public static class Vertex {
		public float x, y, z;
		public float r, g, b;
		public float nx, ny, nz;
		public float u, v;

		public Vertex(float x, float y, float z, float r, float g, float b, float nx, float ny, float nz, float u, float v) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = r;
			this.g = g;
			this.b = b;
			this.nx = nx;
			this.ny = ny;
			this.nz = nz;
			this.u = u;
			this.v = v;
		}

		public void addToBufferBuilder(BufferBuilder builder) {
			VertexFormat vf = builder.getVertexFormat();
			for (VertexFormatElement element : vf.getElements()) {
				switch (element.getUsage()) {
					case UV:
						builder.tex(this.u, this.v);
						break;
					case COLOR:
						builder.color(this.r, this.g, this.b, 1f);
						break;
					case NORMAL:
						builder.normal(this.nx, this.ny, this.nz);
						break;
					case POSITION:
						builder.pos(this.x, this.y, this.z);
						break;
					default:
						break;

				}
			}
			builder.endVertex();
		}

	}

	public static class Face {
		public int drawMode;
		public List<Vertex> vertices;

		public Face(int drawMode) {
			this.drawMode = drawMode;
			vertices = new ArrayList<>();
		}
	}

	public static class Part {
		public List<Face> faces;
		public Vector3f origin;
		public String parent;

		public Part() {
			this.faces = new ArrayList<>();
			origin = new Vector3f(0,0,0);
			parent = "";
		}

		public void draw() {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder builder = tessellator.getBuffer();
			for(Face f : faces) {
				GlStateManager.glBegin(f.drawMode);
				builder.begin(f.drawMode, DefaultVertexFormats.POSITION_TEX_NORMAL);
				for(Vertex v : f.vertices) {
					v.addToBufferBuilder(builder);
				}
				GlStateManager.glEnd();
				tessellator.draw();
			}
		}
	}

	public static Map<String, Part> getPartsFromFile(ResourceLocation location) throws IOException {
		IResource res = Minecraft.getMinecraft().getResourceManager().getResource(location);
		InputStreamReader stream = new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(stream);
		Map<String, Part> parts = new HashMap<>();

		String line = reader.readLine();
		List<Triple<Float, Float, Float>> positions = new ArrayList<>();
		List<Triple<Float, Float, Float>> normals = new ArrayList<>();
		List<Pair<Float, Float>> texCoords = new ArrayList<>();
		String activePartKey = "";
		while (line != null) {
			if (line.startsWith("v ")) {
				String[] split = line.split(" ");
				positions.add(Triple.of(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
			} else if (line.startsWith("vt ")) {
				String[] split = line.split(" ");
				texCoords.add(Pair.of(Float.parseFloat(split[1]), Float.parseFloat(split[2])));
			} else if (line.startsWith("vn ")) {
				String[] split = line.split(" ");
				normals.add(Triple.of(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
			} else if (line.startsWith("f ")) {
				Part part = parts.get(activePartKey);
				if (part != null) {
					String[] splitF = line.split(" ");
					int drawMode = GL11.GL_QUADS;
					if (splitF.length == 4) {
						drawMode = GL11.GL_TRIANGLES;
					} else if (splitF.length > 5) {
						drawMode = GL11.GL_POLYGON;
					}
					Face f = new Face(drawMode);
					for (int i = 1; i < splitF.length; i++) {
						String[] splitV = splitF[i].split("/");
						Triple<Float, Float, Float> pos = positions.get(Integer.parseInt(splitV[0])-1);
						Pair<Float, Float> tex = texCoords.get(Integer.parseInt(splitV[1])-1);
						Triple<Float, Float, Float> nor = normals.get(Integer.parseInt(splitV[2])-1);
						Vertex v = new Vertex(pos.getLeft(), pos.getMiddle(), pos.getRight(),
								1f, 1f, 1f,
								nor.getLeft(), nor.getMiddle(), nor.getRight(),
								tex.getLeft(), tex.getRight());
						f.vertices.add(v);
					}
					part.faces.add(f);
				}
			} else if (line.startsWith("o ")) {
				String name = line.substring(2);
				activePartKey = name;
				Part part = new Part();
				parts.put(name, part);
			} else if (line.startsWith("origin ")) {
				Part part = parts.get(activePartKey);
				if (part != null) {
					String[] split = line.split(" ");
					if(split.length == 2) {
						Triple<Float, Float, Float> or = positions.get(Integer.parseInt(split[1]));
						Vector3f origin = new Vector3f(or.getLeft(), or.getMiddle(), or.getRight());
						part.origin = origin;
					}
					else {
						Vector3f origin = new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
						part.origin = origin;
					}
				}
			} else if (line.startsWith("parent ")) {
				Part part = parts.get(activePartKey);
				if (part != null) {
					String[] split = line.split(" ");
					part.parent = split[1];
				}
			}
			line = reader.readLine();
		}
		return parts;
	}

}
