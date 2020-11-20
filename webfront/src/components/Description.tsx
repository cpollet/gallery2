import React from "react";
import {Tag as ApiTag} from "../API";

interface PropsType {
    children: string,
    tagRegions: ApiTag[]
}

export default function Description({children, tagRegions}: PropsType) {
    function flagTags(str: string, tags: ApiTag[]) {
        type Fragment = {
            key: number,
            str: string,
            tag: boolean
        }

        const sortedTags = tags.sort((a: ApiTag, b: ApiTag) => a.start - b.start);
        let nextFragmentStartIndex = 0;
        let fragments: Fragment[] = [];

        for (let tagIndex = 0; tagIndex < tags.length; tagIndex++) {
            fragments.push({
                key: tagIndex * 2,
                str: str.substring(nextFragmentStartIndex, sortedTags[tagIndex].start),
                tag: false
            }, {
                key: tagIndex * 2 + 1,
                str: str.substring(sortedTags[tagIndex].start, sortedTags[tagIndex].end),
                tag: true
            });
            nextFragmentStartIndex = sortedTags[tagIndex].end;
        }
        fragments.push({
            key: tags.length * 2,
            str: str.substr(nextFragmentStartIndex),
            tag: false
        });

        function renderFragment(f: Fragment) {
            return (
                <span key={f.key}>
                    {
                        f.tag ?
                            <a href={f.str.substr(1)}>{f.str}</a> :
                            <>{f.str}</>
                    }
                </span>
            );
        }

        return (
            fragments
                .filter((f) => f.str !== "")
                .map(f => renderFragment(f))
        );
    }

    return (
        <>
            {flagTags(children, tagRegions)}
        </>
    );
}
