package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.InvalidBlockStateException;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FormationUtils {

	/**
	 * A list containing all found diagrams in the folders
	 */
	public static final Map<ResourceLocation, FormationDiagram> DIAGRAMS = new HashMap<>();

	private static final Splitter COMMA_SPLITTER = Splitter.on(",");
	private static final Splitter EQUAL_SPLITTER = Splitter.on("=").limit(2);
	private static final Splitter NUMERAL_SPLITTER = Splitter.on("#").limit(2);

	/**
	 * Loads all diagrams from files
	 */
	public static void init() {
		try {
			URI assets = Objects.requireNonNull(FormationUtils.class.getClassLoader().getResource("assets/wuxiacraft/formations")).toURI();
			if (assets.getScheme().equals("jar")) {
				//ok now i know what to do
				String PathToZip = assets.getSchemeSpecificPart();
				PathToZip = PathToZip.substring("file:".length());
				PathToZip = PathToZip.substring(0, PathToZip.lastIndexOf("!"));
				ZipFile zip = new ZipFile(PathToZip);
				ZipEntry entry = null;
				Enumeration<? extends ZipEntry> entries = zip.entries();

				while (entries.hasMoreElements()) {
					entry = entries.nextElement();
					if (entry.getName().startsWith("assets/wuxiacraft/formations/") && !entry.isDirectory())
						getDiagramFromZipEntry(zip, entry);
				}
			} else {
				Path pathToAssetsDir = Paths.get(assets);
				File assetsDir = pathToAssetsDir.toFile();
				getDiagramsFromDirectory(assetsDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a {@link File} that is a directory and scans it whole for files that contains diagrams
	 *
	 * @param directory The directory to be scanned
	 */
	private static void getDiagramsFromDirectory(File directory) {
		File[] listFiles = directory.listFiles();
		if (listFiles != null) {
			for (File file : listFiles) {
				if (file.isDirectory()) {
					getDiagramsFromDirectory(file);
				} else {
					getDiagramFromFile(file);
				}
			}
		}
	}

	/**
	 * Gets a {@link ZipEntry} and try to read it into a diagram
	 *
	 * @param entry The file to be read
	 */
	public static void getDiagramFromZipEntry(ZipFile file, ZipEntry entry) {
		try {
			InputStream is = file.getInputStream(entry);
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String allText = s.hasNext() ? s.next() : "";
			Gson gson = new Gson();
			FormationJsonFormat result = gson.fromJson(allText, FormationJsonFormat.class);
			FormationDiagram diagram = new FormationDiagram(new ResourceLocation(result.formationName));
			for (FormationJsonFormat.PositionPair position : result.positions) {
				IBlockState blockState = getBlockStateWithProperties(position.blockState);
				BlockPos pos = new BlockPos(position.x, position.y, position.z);
				diagram.addPosition(pos, blockState);
			}
			DIAGRAMS.put(diagram.getFormationName(), diagram);
			WuxiaCraft.logger.info("Added diagram: " + diagram.getFormationName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a {@link File} that isn't a directory, then try to read it into a diagram, and automatically adds to {@link FormationUtils#DIAGRAMS}
	 *
	 * @param file The file to be read
	 */
	public static void getDiagramFromFile(File file) {
		try {
			Reader reader = new FileReader(file);
			Gson gson = new Gson();
			FormationJsonFormat result = gson.fromJson(reader, FormationJsonFormat.class);
			FormationDiagram diagram = new FormationDiagram(new ResourceLocation(result.formationName));
			for (FormationJsonFormat.PositionPair position : result.positions) {
				IBlockState blockState = getBlockStateWithProperties(position.blockState);
				BlockPos pos = new BlockPos(position.x, position.y, position.z);
				diagram.addPosition(pos, blockState);
			}
			DIAGRAMS.put(diagram.getFormationName(), diagram);
			WuxiaCraft.logger.info("Added diagram: " + diagram.getFormationName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transform a resource location + variations into a {@link IBlockState}
	 *
	 * @param args string to be transformed
	 * @return if found, a block state to be expected in the formation, else null
	 */
	private static IBlockState getBlockStateWithProperties(String args) {
		IBlockState state = null;
		Iterator<String> iterator = NUMERAL_SPLITTER.split(args).iterator();
		String blockResourceName = iterator.hasNext() ? iterator.next() : "";
		String states = iterator.hasNext() ? iterator.next() : "";
		ResourceLocation blockLocation = new ResourceLocation(blockResourceName);
		Block block = ForgeRegistries.BLOCKS.getValue(blockLocation);
		Map<IProperty<?>, Comparable<?>> stateMap = null;
		try {
			stateMap = getBlockStatePropertyValueMap(block, states);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (stateMap != null && block != null) {
			state = block.getDefaultState();
			for (Map.Entry<IProperty<?>, Comparable<?>> entry : stateMap.entrySet()) {
				state = getBlockState(state, entry.getKey(), entry.getValue());
			}
		}
		return state;
	}

	/**
	 * Adds properties to a base block state
	 *
	 * @param blockState base block state
	 * @param property   property to be added
	 * @param value      value of the property
	 * @param <T>        the type of value the property stores
	 * @return the block state added of the property
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> IBlockState getBlockState(IBlockState blockState, IProperty<T> property, Comparable<?> value) {
		return blockState.withProperty(property, (T) value);
	}

	/**
	 * Get block state from block with custom properties
	 *
	 * @param block The base block for look into properties
	 * @param args  A string filled with variations and values
	 * @return Block state if found
	 * @throws InvalidBlockStateException Block doesn't have this properties
	 */
	private static Map<IProperty<?>, Comparable<?>> getBlockStatePropertyValueMap(Block block, String args) throws InvalidBlockStateException {
		Map<IProperty<?>, Comparable<?>> map = Maps.newHashMap();

		if ("default".equals(args) || "".equals(args)) {
			return block.getDefaultState().getProperties();
		} else {
			BlockStateContainer blockstatecontainer = block.getBlockState();
			Iterator<String> iterator = COMMA_SPLITTER.split(args).iterator();

			while (true) {
				if (!iterator.hasNext()) {
					return map;
				}

				String s = iterator.next();
				Iterator<String> iterator1 = EQUAL_SPLITTER.split(s).iterator();

				if (!iterator1.hasNext()) {
					break;
				}

				IProperty<?> property = blockstatecontainer.getProperty(iterator1.next());

				if (property == null || !iterator1.hasNext()) {
					break;
				}

				Comparable<?> comparable = getValueHelper(property, iterator1.next());

				if (comparable == null) {
					break;
				}

				map.put(property, comparable);
			}

			throw new InvalidBlockStateException("loader.wuxiacraft.blockstate.invalid", args, Block.REGISTRY.getNameForObject(block));
		}
	}

	/**
	 * Helps with block state values
	 *
	 * @param property target property
	 * @param args     possibly the values
	 * @param <T>      The type of property
	 * @return return the value of the property
	 */
	@Nullable
	private static <T extends Comparable<T>> T getValueHelper(IProperty<T> property, String args) {
		return property.parseValue(args).orNull();
	}

	/**
	 * Rotates a diagram position offset to a direction
	 *
	 * @param pos       Position offset to be rotated
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
	 *
	 * @param pos       Position offset from formation source
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

	/**
	 * Searches the world around source for a compatible formation diagram
	 *
	 * @param worldIn the world to be searched
	 * @param source  the formation core position
	 * @return First diagram found, or null if none
	 */
	@Nonnull
	public static Pair<FormationDiagram, EnumFacing> searchWorldForFormations(World worldIn, BlockPos source) {
		FormationDiagram selected = null;
		EnumFacing direction = EnumFacing.NORTH;
		boolean found;
		for (FormationDiagram diagram : DIAGRAMS.values()) {
			for (EnumFacing facing : EnumFacing.values()) {
				if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
				found = true;
				for (Pair<BlockPos, IBlockState> pair : diagram.positions) {
					BlockPos relativePos = rotateBlockPos(pair.getKey(), facing);
					BlockPos pos = new BlockPos(source.getX() + relativePos.getX(), source.getY() + relativePos.getY(), source.getZ() + relativePos.getZ());
					IBlockState toTest = worldIn.getBlockState(pos);
					if (pair.getValue().getBlock() == toTest.getBlock()) { // if are the same block, this way we could get any state case default
						if (!(toTest.equals(pair.getValue()) // if states are equals
								|| pair.getValue().getBlock().getDefaultState() == pair.getValue())) { // or if state in diagrams is default state, then any
							found = false;
							break;
						}
					} else {
						found = false;
						break;
					}

				}
				if (found) {
					selected = diagram;
					break;
				}
			}
			if (selected != null) break;
		}
		return Pair.of(selected, direction);
	}

	@Nullable
	public static Formation getFormationFromResource(ResourceLocation resource) {
		Formation result = null;
		if (resource.getResourceDomain().equals(WuxiaCraft.MOD_ID)) {
			for (Formation formation : Formations.FORMATIONS) {
				if (formation.getUName().equals(resource.getResourcePath())) {
					result = formation;
					break;
				}
			}
		}
		return result;
	}

	public static class FormationDiagram {
		private final ResourceLocation formationName;
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
			for (Pair<BlockPos, IBlockState> pair : this.positions) {
				blockPosList.add(pair.getKey());
			}
			return blockPosList;
		}

		@Nullable
		public IBlockState getBlockState(BlockPos pos) {
			IBlockState state = null;
			for (Pair<BlockPos, IBlockState> pair : this.positions) {
				if (pos.equals(pair.getKey())) {
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
