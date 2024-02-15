import React from 'react';
import ReactTextareaAutocomplete from "@webscopeio/react-textarea-autocomplete";
import "@webscopeio/react-textarea-autocomplete/style.css";

interface PropsType {
    text: string,
    tags: {
        tag: string,
        count: number
    }[],
    tagsToDisplay?: number,
    onChange: (text: string) => void
}

export default function AutocompleteTextarea(props: PropsType) {
    const [text, setText] = React.useState(props.text)

    const dataProvider = (token: string): { name: string, char: string }[] => props.tags
        .filter(e => token === '' || e.tag.toLowerCase().indexOf(token.toLowerCase()) >= 0)
        .sort((a, b) => b.count - a.count)
        .slice(0, props.tagsToDisplay || 5)
        .map(t => {
            return {name: t.tag, char: t.tag}
        });

    return (
        <ReactTextareaAutocomplete
            loadingComponent={() => <span>Loading</span>}
            minChar={0}
            trigger={{
                "#": {
                    dataProvider: dataProvider,
                    component: (e) => <div>{e.entity.char}</div>,
                    output: (item) => '#' + item.char
                }
            }}
            className="form-control"
            style={{fontSize: "1rem", padding: 0}}
            rows={3}
            value={text}
            onChange={e => setText(e.target.value)}
            onBlur={() => props.onChange(text)}
        />
    );
}