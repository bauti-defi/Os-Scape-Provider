package org.osscape.api.injectables;

import org.bot.Engine;
import org.bot.provider.osrs.models.Model;
import org.bot.provider.osrs.models.ModelCallBack;
import org.bot.util.injection.Injector;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Ethan on 7/9/2017.
 */

public class ModelInjector implements Injector {


	@SuppressWarnings("deprecation")
	private static void callBack(MethodNode mn) {

		InsnList nl = new InsnList();
		boolean b = false;
		for (int i = 0; i < mn.instructions.size(); i++) {
			AbstractInsnNode abs = mn.instructions.get(i);
			if (abs.getOpcode() == Opcodes.ASTORE && !b) {
				VarInsnNode varInsnNode = (VarInsnNode) abs;
				nl.add(abs);
				if (varInsnNode.var == 10) {
					System.out.println("Found place to inject ModelCallback");
					b = true;
					nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
					nl.add(new TypeInsnNode(Opcodes.NEW, Model.class.getCanonicalName().replace('.', '/')));
					nl.add(new InsnNode(Opcodes.DUP));
					nl.add(new VarInsnNode(Opcodes.ALOAD, 10));
					nl.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Model.class.getCanonicalName().replace('.', '/'), "<init>", "(Ljava/lang/Object;)V"));
					nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
					nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, (ModelCallBack.class.getCanonicalName().replace('.', '/')), "add", "(L" + Model.class.getCanonicalName().replace('.', '/') + ";Ljava/lang/Object;)V"));
				}
			} else {
				nl.add(abs);
			}
		}
		mn.instructions = nl;
		mn.visitMaxs(0, 0);
		mn.visitEnd();
	}

	@Override
	public boolean canRun(ClassNode classNode) {
		if (classNode.name.length() > 0) {
			if (Engine.getReflectionEngine().getClassName("ModelHeight").length() > 0) {
				if (Engine.getReflectionEngine().getClassName("ModelHeight").replaceAll(Pattern.quote("."), "/").equals(classNode.name)) {
					return true;
				}

			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(ClassNode classNode) {

		for (MethodNode methodNode : (List<MethodNode>) classNode.methods)
			for (AbstractInsnNode abstractInsn : methodNode.instructions.toArray()) {
				if (abstractInsn instanceof MethodInsnNode) {
					if (((MethodInsnNode) abstractInsn).desc.contains(Engine.getReflectionEngine().getClassName("ModelVertX").replaceAll(Pattern.quote("."), "/"))) {
						callBack(methodNode);
						break;
					}
				}
			}
	}

}