THISDIR="$(dirname $(readlink -f $0))"

read -p "item model name? " MODEL_NAME

echo "{
	\"parent\":\"item/generated\",
	\"textures\":{
		\"layer0\":\"wuxiacraft:items/$MODEL_NAME\"
	}
}" >> $THISDIR/src/main/resources/assets/wuxiacraft/models/item/$MODEL_NAME.json

git add $THISDIR/src/main/resources/assets/wuxiacraft/models/item/$MODEL_NAME.json