import React from "react";
import {AvailableTag as ApiAvailableTag} from "../API";

interface PropsType {
    renderer: (tags: ApiAvailableTag[]) => JSX.Element
}

export default function AvailableTagsProvider({renderer}: PropsType) {
    const [tags,setTags] = React.useState<ApiAvailableTag[]>([]);

    React.useEffect(fetchAvailableTagsList, []);

    function fetchAvailableTagsList() {
                setTags([
                    {tag: "Lenna", count: 10},
                    {tag: "Lena", count: 10},
                    {tag: "1973", count: 20},
                    {tag: "Swedish", count: 10},
                ]);
    }

    return renderer(tags);
}