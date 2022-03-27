THISDIR="$(dirname $(readlink -f $0))"

read -p "block model name? " MODEL_NAME

echo "{
	\"parent\": \"wuxiacraft:block/$MODEL_NAME\"
}" >> $THISDIR/src/main/resources/assets/wuxiacraft/models/item/$MODEL_NAME.json

git add $THISDIR/src/main/resources/assets/wuxiacraft/models/item/$MODEL_NAME.json