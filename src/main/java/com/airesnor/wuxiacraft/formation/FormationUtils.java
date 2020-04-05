package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.InvalidBlockStateException;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

public class FormationUtils {

	public static final List<FormationDiagram> DIAGRAMS = new ArrayList<>();

	private static final Splitter COMMA_SPLITTER = Splitter.on(",");
	private static final Splitter EQUAL_SPLITTER = Splitter.on("=").limit(2);
	private static final Splitter NUMERAL_SPLITTER = Splitter.on("#").limit(2);

	public static void init() {
		URL assets = Minecraft.class.getClassLoader().getResource("assets/wuxiacraft/formations");
		if(assets != null) {
			File assetsDir = new File(assets.getFile());
			getDiagramsFromDirectory(assetsDir);
		} else {
			WuxiaCraft.logger.error("Couldn't find assets folder. Not found but without exception");
		}
	}

	private static void getDiagramsFromDirectory(File directory) {
		for (File file : Objects.requireNonNull(directory.listFiles())) {
			if(file.isDirectory()) {
				getDiagramsFromDirectory(file);
			}
			else {
				getDiagramFromFile(file);
			}
		}
	}

	public static void getDiagramFromFile(File file) {
		try {
			Reader reader = new FileReader(file);
			Gson gson = new Gson();
			FormationJsonFormat result = gson.fromJson(reader, FormationJsonFormat.class);
			FormationDiagram diagram = new FormationDiagram(new ResourceLocation(result.formationName));
			for(FormationJsonFormat.PositionPair position : result.positions) {
				IBlockState blockState = getBlockStateWithProperties(position.blockState);
				BlockPos pos = new BlockPos(position.x, position.y, position.z);
				diagram.addPosition(pos, blockState);
			}
			DIAGRAMS.add(diagram);
			WuxiaCraft.logger.info("Added diagram: " + diagram.getFormationName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static IBlockState getBlockStateWithProperties(String args) {
		IBlockState state = null;
		Iterator<String> iterator = NUMERAL_SPLITTER.split(args).iterator();
		String blockResourceName = iterator.hasNext() ? iterator.next() : "";
		String states =  iterator.hasNext() ? iterator.next() : "";
		ResourceLocation blockLocation = new ResourceLocation(blockResourceName);
		Block block = ForgeRegistries.BLOCKS.getValue(blockLocation);
		Map<IProperty<?>, Comparable<? >> stateMap = null;
		try {
			stateMap = getBlockStatePropertyValueMap(block, states);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (stateMap != null && block != null) {
			state = block.getDefaultState();
			for (Map.Entry< IProperty<?>, Comparable<? >> entry : stateMap.entrySet()) {
				state = getBlockState(state, entry.getKey(), entry.getValue());
			}
		}
		return state;
	}

	private static <T extends Comparable<T>> IBlockState getBlockState(IBlockState blockState, IProperty<T> property, Comparable<?> value)
	{
		return blockState.withProperty(property, (T)value);
	}

	private static Map<IProperty<?>, Comparable<? >> getBlockStatePropertyValueMap(Block block, String args) throws InvalidBlockStateException
	{
		Map < IProperty<?>, Comparable<? >> map = Maps.newHashMap();

		if ("default".equals(args) || "".equals(args))
		{
			return block.getDefaultState().getProperties();
		}
		else
		{
			BlockStateContainer blockstatecontainer = block.getBlockState();
			Iterator<String> iterator = COMMA_SPLITTER.split(args).iterator();

			while (true)
			{
				if (!iterator.hasNext())
				{
					return map;
				}

				String s = iterator.next();
				Iterator<String> iterator1 = EQUAL_SPLITTER.split(s).iterator();

				if (!iterator1.hasNext())
				{
					break;
				}

				IProperty<?> property = blockstatecontainer.getProperty(iterator1.next());

				if (property == null || !iterator1.hasNext())
				{
					break;
				}

				Comparable<?> comparable = getValueHelper(property, iterator1.next());

				if (comparable == null)
				{
					break;
				}

				map.put(property, comparable);
			}

			throw new InvalidBlockStateException("loader.wuxiacraft.blockstate.invalid", args, Block.REGISTRY.getNameForObject(block));
		}
	}

	@Nullable
	private static <T extends Comparable<T>> T getValueHelper(IProperty<T> property, String args)
	{
		return (T)(property.parseValue(args).orNull());
	}

	/**
	 * Rotates a diagram position offset to a direction
	 * @param pos Position offset to be rotated
	 * @param direction Direction to which will be rotate
	 * @return A rotated position offset
	 */
	public static BlockPos rotateBlockPos(BlockPos pos, EnumFacing direction) {
		BlockPos value;
		switch (direction) {
			case SOUTH:
				value = new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
				break;
			case EAST:
				value = new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
				break;
			case WEST:
				value = new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
				break;
			case NORTH:
			case UP:
			case DOWN:
			default:
				value = pos;
				break;
		}
		return value;
	}

	/**
	 * Rotates a formation position offset back to how it's set on diagram (north-oriented);
	 * @param pos Position offset from formation source
	 * @param direction Direction that formation is set
	 * @return A north-oriented position offset
	 */
	public static BlockPos rotateBlockPosBack(BlockPos pos, EnumFacing direction) {
		BlockPos value;
		switch (direction) {
			case SOUTH:
				value = new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
				break;
			case EAST:
				value = new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
				break;
			case WEST:
				value = new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
				break;
			case NORTH:
			case UP:
			case DOWN:
			default:
				value = pos;
				break;
		}
		return value;
	}

	@Nonnull
	public static Pair<FormationDiagram, EnumFacing> searchWorldForFormations(World worldIn, BlockPos source) {
		FormationDiagram selected = null;
		EnumFacing direction = EnumFacing.NORTH;
		boolean found = false;
		for(EnumFacing facing : EnumFacing.values()) {
			if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
			for (FormationDiagram diagram : DIAGRAMS) {
				found = true;
				for (Pair<BlockPos, IBlockState> pair : diagram.positions) {
					BlockPos pos = rotateBlockPos(pair.getKey(), facing);
					IBlockState toTest = worldIn.getBlockState(pos);
					if (toTest.equals(pair.getValue())) {
						found = false;
						break;
					}
				}
				if (found) {
					selected = diagram;
					break;
				}
			}
			if(selected != null) break;
		}
		return Pair.of(selected, direction);
	}

	@Nullable
	public static Formation getFormationFromResource(ResourceLocation resource) {
		Formation result = null;
		if(resource.getResourceDomain().equals(WuxiaCraft.MOD_ID)) {
			for(Formation formation : Formations.FORMATIONS) {
				if(formation.getUName().equals(resource.getResourcePath())) {
					result = formation;
					break;
				}
			}
		}
		return result;
	}

	public static class FormationDiagram {
		private ResourceLocation formationName;
		public final List<Pair<BlockPos, IBlockState>> positions = new ArrayList<>();

		public FormationDiagram(ResourceLocation formationName) {
			this.formationName = formationName;
		}

		public ResourceLocation getFormationName() {
			return formationName;
		}

		public void addPosition(BlockPos pos, IBlockState block) {
			this.positions.add(Pair.of(pos, block));
		}

		@Nonnull
		public List<BlockPos> getAllBlockPos() {
			List<BlockPos> blockPosList = new ArrayList<>();
			for(Pair<BlockPos, IBlockState> pair : this.positions) {
				blockPosList.add(pair.getKey());
			}
			return blockPosList;
		}

		@Nullable
		public IBlockState getBlockState(BlockPos pos) {
			IBlockState state = null;
			for(Pair<BlockPos, IBlockState> pair : this.positions) {
				if(pos.equals(pair.getKey())) {
					state = pair.getValue();
				}
			}
			return state;
		}
	}

	public static class FormationJsonFormat {
		public String name;
		public String formationName;
		public List<PositionPair> positions;

		public static class PositionPair {
			public int x;
			public int y;
			public int z;
			public String blockState;
		}
	}

}
