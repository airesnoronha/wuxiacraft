package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.alchemy.Recipe;
import com.airesnor.wuxiacraft.alchemy.Recipes;
import com.airesnor.wuxiacraft.networking.AddRecipeItemMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SpawnParticleMessage;
import com.airesnor.wuxiacraft.utils.MathUtils;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class CauldronTileEntity extends TileEntity implements ITickable {

	private boolean hasFirewood;
	private boolean hasWater;

	private boolean isLit;
	private float timeLit;
	private float maxTimeLit;

	private float burnSpeed;
	private float temperature;

	private float maxTemperature;

	private int cookTime;

	private int burningTime;

	private Recipe activeRecipe;

	private final List<Pair<Float, Item>> recipeInputs;

	private EnumCauldronState cauldronState;

	public CauldronTileEntity() {
		this.hasFirewood = false;
		this.hasWater = false;
		this.isLit = false;
		this.burnSpeed = 0.6f;
		this.temperature = 30f;
		this.timeLit = 0;
		this.maxTimeLit = 0;
		this.maxTemperature = 2000f;
		this.recipeInputs = new ArrayList<>();
		this.cauldronState = EnumCauldronState.EMPTY;
		this.cookTime = 0;
		this.burningTime = 0;
		this.activeRecipe = null;
	}

	public Recipe getActiveRecipe() {
		return this.activeRecipe;
	}

	public float getTimeLit() {
		return timeLit;
	}

	public float getBurnSpeed() {
		return burnSpeed;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getMaxTemperature() {
		return maxTemperature;
	}

	@SuppressWarnings("unused")
	public CauldronTileEntity setMaxTemperature(float maxTemperature) {
		this.maxTemperature = maxTemperature;
		return this;
	}

	public void setTemperature(float temperature) {
		this.temperature = Math.min(this.maxTemperature, Math.max(30f, temperature));
	}

	public boolean isLit() {
		return isLit;
	}

	public void setLit(boolean lit) {
		isLit = lit;
	}

	public boolean isHasFirewood() {
		return hasFirewood;
	}

	public void setHasFirewood(boolean hasFirewood) {
		this.hasFirewood = hasFirewood;
	}

	public boolean isHasWater() {
		return hasWater;
	}

	public void setHasWater(boolean hasWater) {
		this.hasWater = hasWater;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setHasFirewood(compound.getBoolean("has_firewood"));
		setHasWater(compound.getBoolean("has_water"));
		setLit(compound.getBoolean("is_lit"));
		timeLit = (compound.getFloat("lit_time"));
		maxTimeLit = (compound.getFloat("max_lit_time"));
		setTemperature(compound.getFloat("temperature"));
		maxTemperature = (compound.getFloat("max_temperature"));
		burnSpeed = compound.getFloat("burn_speed");
		cookTime = compound.getInteger("cook_time");
		burningTime = compound.getInteger("burning_time");
		int inputsSize = compound.getInteger("inputs_size");
		this.recipeInputs.clear();
		for (int i = 0; i < inputsSize; i++) {
			float temp = compound.getFloat("temp-" + i);
			Item item = new ItemStack(compound.getCompoundTag("item-" + i)).getItem();
			this.recipeInputs.add(Pair.of(temp, item));
		}
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("has_firewood", isHasFirewood());
		compound.setBoolean("has_water", isHasWater());
		compound.setBoolean("is_lit", isLit());
		compound.setFloat("lit_time", timeLit);
		compound.setFloat("max_lit_time", maxTimeLit);
		compound.setFloat("temperature", temperature);
		compound.setFloat("max_temperature", maxTemperature);
		compound.setFloat("burn_speed", burnSpeed);
		compound.setInteger("cook_time", cookTime);
		compound.setInteger("burning_time", burningTime);
		compound.setInteger("inputs_size", this.recipeInputs.size());
		for (int i = 0; i < this.recipeInputs.size(); i++) {
			compound.setFloat("temp-" + i, this.recipeInputs.get(i).getLeft());
			NBTTagCompound tag = new NBTTagCompound();
			new ItemStack(this.recipeInputs.get(i).getRight(), 1).writeToNBT(tag);
			compound.setTag("item-" + i, tag);
		}
		return compound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTagCompound);
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update() {
		if (isLit()) {
			int light = Math.min(15, Math.max(world.getLight(getPos()), (int) this.burnSpeed));
			world.setLightFor(EnumSkyBlock.BLOCK, getPos(), light);
			this.timeLit -= this.burnSpeed;
			this.temperature += this.burnSpeed;
			this.burnSpeed = Math.max(0.4f, this.burnSpeed - 0.03f);
			if (this.timeLit < 0) {
				this.resetBurnSettings();
			}
			if (!world.isRemote) {
				Random rand = new Random();
				int qty = (int) (2f * this.burnSpeed);
				for (int i = 0; i < qty; i++) {
					float x = getPos().getX() + 0.5f + (2 * rand.nextFloat() - 1f) * 0.3f;
					float z = getPos().getZ() + 0.5f + (2 * rand.nextFloat() - 1f) * 0.3f;
					float y = getPos().getY();
					SpawnParticleMessage spm = new SpawnParticleMessage(EnumParticleTypes.FLAME, false, x, y, z, 0, 0.01f, 0, 0);
					SkillUtils.sendMessageWithinRange((WorldServer) world, getPos(), 16, spm);
				}
			}
		} else {
			world.setLightFor(EnumSkyBlock.BLOCK, getPos(), 0);
		}
		setTemperature(this.temperature * 0.995f);
		checkLightAround();

		this.activeRecipe = null;

		if (!this.recipeInputs.isEmpty()) {
			this.setHasWater(true);
			List<Recipe> candidates = Recipes.getRecipeCandidatesByInput(recipeInputs);
			if (!candidates.isEmpty()) {
				Recipe definitive = Recipes.getDefinitiveRecipe(recipeInputs, candidates);
				if (definitive != null) {
					this.activeRecipe = definitive;
					if (MathUtils.between(this.temperature, definitive.getCookTemperatureMin(), definitive.getCookTemperatureMax())) {
						this.cookTime++;
						this.cauldronState = EnumCauldronState.COOKING;
						if (this.cookTime >= definitive.getCookTime()) {
							emptyCauldron();
							spawnRecipeOutput(definitive);
						}
					} else if (this.temperature < definitive.getCookTemperatureMin()) {
						this.cauldronState = EnumCauldronState.COOLING;
					} else if (this.temperature > definitive.getCookTemperatureMax()) {
						this.cauldronState = EnumCauldronState.BURNING;
						this.burningTime++;
					}
				} else {
					this.cauldronState = EnumCauldronState.HAS_RECIPE;
				}
			} else {
				this.cauldronState = EnumCauldronState.WRONG_RECIPE;
				if (this.temperature > 0.4f * maxTemperature) {
					this.burningTime++;
				}
			}
		} else {
			this.setHasWater(false);
			this.cauldronState = EnumCauldronState.EMPTY;
		}
		if (this.burningTime > 20 * 15) {
			this.explode();
		}
	}

	public void wiggleFan(float strength, float maxFanStrength) {
		this.burnSpeed = Math.min(this.burnSpeed + strength, maxFanStrength);
	}

	public void addWood(int time) {
		this.hasFirewood = true;
		this.timeLit = time;
		this.maxTimeLit = time;
	}

	public void setOnFire() {
		this.isLit = true;
		this.burnSpeed = 1.2f;
	}

	private void resetBurnSettings() {
		this.isLit = false;
		this.burnSpeed = 0.4f;
		this.timeLit = 0;
		this.hasFirewood = false;
	}

	public void emptyCauldron() {
		this.recipeInputs.clear();
		this.cookTime = 0;
		this.burningTime = 0;
	}

	public void checkLightAround() {
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().up());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().down());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().north());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().east());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().south());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().west());
		getWorld().markBlockRangeForRenderUpdate(getPos(), new BlockPos(16, 16, 16));
	}

	public void prepareToDie() {
		world.setLightFor(EnumSkyBlock.BLOCK, getPos(), 0);
		checkLightAround();
	}

	public void addRecipeInput(Item item) {
		if(this.world.isRemote) {
			float temperature  = this.getTemperature();
			this.recipeInputs.add(Pair.of(temperature, item));
			WuxiaCraft.logger.info("Adding item at " + String.format("%.2f degrees", temperature));
			NetworkWrapper.INSTANCE.sendToServer(new AddRecipeItemMessage(this.getPos(), item, temperature));
		}
	}
	public void addServerRecipeInput(Item item, float temperature) {
		if(!this.world.isRemote) {
			this.recipeInputs.add(Pair.of(temperature, item));
			WuxiaCraft.logger.info("Adding item on server at " + String.format("%.2f degrees",temperature));
		}
	}

	public EnumCauldronState getCauldronState() {
		return this.cauldronState;
	}

	public boolean isAcceptingItems() {
		return this.cauldronState != EnumCauldronState.WRONG_RECIPE;
	}

	private void spawnRecipeOutput(Recipe recipe) {
		if (!world.isRemote) {
			ItemStack output = new ItemStack(recipe.getOutput(), recipe.getYieldResult());
			world.spawnEntity(new EntityItem(world, getPos().getX() + 0.5f, getPos().getY() + 2f, getPos().getZ() + 0.5f, output));
		}
	}

	public float getMaxTimeLit() {
		return maxTimeLit;
	}

	private void explode() {
		if (getWorld() instanceof WorldServer) {
			getWorld().createExplosion(null, getPos().getX() + 0.5, getPos().getY() + 1.5f, getPos().getZ() + 0.5, 3f, true);
		}
		this.emptyCauldron();
	}

	public enum EnumCauldronState {
		HAS_RECIPE(0.9f, 0.6f, 0.2f),
		BURNING(1f, 0.3f, 0.3f),
		COOKING(1f, 1f, 0f),
		COOLING(0.35f, 0.8f, 0.25f),
		WRONG_RECIPE(1f, 0.15f, 0.15f),
		EMPTY(1f, 1f, 1f);

		private final Color color;

		EnumCauldronState(float r, float g, float b) {
			this.color = new Color(r, g, b);
		}

		public Color getColor() {
			return this.color;
		}
	}

}
