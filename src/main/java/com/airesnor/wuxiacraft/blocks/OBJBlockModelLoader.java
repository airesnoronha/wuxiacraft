package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.models.BaseModel;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.resource.IResourceType;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class OBJBlockModelLoader implements ICustomModelLoader {

    public static final OBJBlockModelLoader INSTANCE = new OBJBlockModelLoader();

    private static final String LOCATION = "models/block/obj/";

    private final Map<ResourceLocation, OBJModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(WuxiaCraft.MODID)
                && modelLocation.getResourcePath().startsWith(LOCATION);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        String resourcePath = modelLocation.getResourcePath();
        if (!resourcePath.startsWith(LOCATION)) {
            assert false : "loadModel expected " + LOCATION + " but found " + resourcePath;
        }
        String modelName = resourcePath.substring(LOCATION.length());

        return new BaseModel(modelName);
    }

    public IModel parseOBJ(ResourceLocation modelLocation) throws Exception
    {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if (!cache.containsKey(file))
        {
            IResource resource = null;
            try
            {
                try
                {
                    resource = resourceManager.getResource(file);
                }
                catch (FileNotFoundException e)
                {
                    if (modelLocation.getResourcePath().startsWith("models/block/"))
                        resource = resourceManager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                    else if (modelLocation.getResourcePath().startsWith("models/item/"))
                        resource = resourceManager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                    else throw e;
                }
                OBJModel.Parser parser = new OBJModel.Parser(resource, resourceManager);
                OBJModel model = null;
                try
                {
                    model = parser.parse();
                }
                catch (Exception e)
                {
                    errors.put(modelLocation, e);
                }
                finally
                {
                    cache.put(modelLocation, model);
                }
            }
            finally
            {
                IOUtils.closeQuietly(resource);
            }
        }
        OBJModel model = cache.get(modelLocation);
        if (model == null) throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }

    private IResourceManager resourceManager;
}
