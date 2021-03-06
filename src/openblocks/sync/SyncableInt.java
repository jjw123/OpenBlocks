package openblocks.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SyncableInt implements ISyncableObject {

	private int value = 0;
	private boolean hasChanged = false;
	private int ticksSinceChanged = 0;
	
	public SyncableInt(int value) {
		this.value = value;
	}

	public SyncableInt() {
		this(0);
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
		value = stream.readInt();
	}

	public void modify(int by) {
		setValue(value + by);
	}

	public void setValue(int val) {
		if (val != value) {
			value = val;
			setHasChanged();
		}
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt((Integer)value);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, String name) {
		tag.setInteger(name, value);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag, String name) {
		if (tag.hasKey(name)) {
			value = tag.getInteger(name);
		}
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void resetChangeStatus() {
		hasChanged = false;
		ticksSinceChanged++;
	}

	@Override
	public void setHasChanged() {
		hasChanged = true;
		ticksSinceChanged = 0;
	}
}
