package wolforce;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerVICE extends net.minecraft.inventory.ContainerWorkbench {

	public ContainerVICE(InventoryPlayer inventoryPlayer, World world, BlockPos pos) {
		super(inventoryPlayer, world, pos);
	}

//	// Shift interaction
//	@Override
//	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
//		ItemStack stack = ItemStack.EMPTY;
//		Slot slot = this.inventorySlots.get(slotIndex);
//
//		if (slot != null && slot.getHasStack()) {
//			ItemStack slotStack = slot.getStack();
//			stack = slotStack.copy();
//
//			if (slotIndex == 0) {
//				if (!this.mergeItemStack(slotStack, 10, 46, true)) {
//					return ItemStack.EMPTY;
//				}
//
//				slot.onSlotChange(slotStack, stack);
//			} else if (slotIndex >= 10 && slotIndex < 37) {
//				if (!this.mergeItemStack(slotStack, 1, 10, false)) {
//					return ItemStack.EMPTY;
//				}
//			} else if (slotIndex >= 37 && slotIndex < 46) {
//				if (!this.mergeItemStack(slotStack, 1, 10, false)) {
//					return ItemStack.EMPTY;
//				}
//			} else if (!this.mergeItemStack(slotStack, 10, 46, false)) {
//				return ItemStack.EMPTY;
//			}
//
//			if (slotStack.isEmpty()) {
//				slot.putStack(ItemStack.EMPTY);
//			} else {
//				slot.onSlotChanged();
//			}
//
//			if (slotStack.getCount() == stack.getCount()) {
//				return ItemStack.EMPTY;
//			}
//
//			slot.onTake(player, slotStack);
//		}
//
//		return stack;
//	}

	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		return true;
	}

}
