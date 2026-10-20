package wolforce.vaultopic.client;

import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.Minecraft;

public class Projector {

	private static Minecraft mc = Minecraft.getMinecraft();

	private static FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
	private static IntBuffer viewport = BufferUtils.createIntBuffer(16);
	private static FloatBuffer screen2D = BufferUtils.createFloatBuffer(16);

	static Method setupCameraTransform;

	static {
		// try {
		// setupCameraTransform =
		// EntityRenderer.class.getDeclaredMethod("setupCameraTransform", float.class,
		// int.class);
		// setupCameraTransform.setAccessible(true);
		// } catch (NoSuchMethodException | SecurityException e) {
		// e.printStackTrace();
		// }

		// Thank you AterAnimAvis @ https://github.com/AterAnimAvis
		try {
			setupCameraTransform = net.minecraft.client.renderer.EntityRenderer.class
					.getDeclaredMethod("setupCameraTransform", float.class, int.class);
			setupCameraTransform.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException ignored) {
			try {
				setupCameraTransform = net.minecraft.client.renderer.EntityRenderer.class
						.getDeclaredMethod("func_78479_a", float.class, int.class);
				setupCameraTransform.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadMatrixes(float partialTicks) {
		modelMatrix.clear();
		projMatrix.clear();
		viewport.clear();
		screen2D.clear();

		GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();

		try {
			setupCameraTransform.invoke(mc.entityRenderer, partialTicks, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();

		GL11.glPopAttrib();
	}

	public static float[] getScrXY(float x, float y, float z, float partialTicks) {

		if (Project.gluProject(x, y, z, modelMatrix, projMatrix, viewport, screen2D)) {
			return new float[] { //
					screen2D.get(0) / screen2D.get(2), //
					screen2D.get(1) / screen2D.get(2), //
					screen2D.get(2) //
			};
		}
		return null;

	}

}
