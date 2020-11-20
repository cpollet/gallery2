import React from "react";
import {Picture as ApiPicture} from "../API";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Description from "./Description";
import {Mention, MentionsInput} from 'react-mentions';
import Form from "react-bootstrap/Form";

interface PropsType {
    pictures: ApiPicture[],
    editMode: boolean
}

export default function ListPictures({pictures, editMode}: PropsType) {
    function thumbnail(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "thumbnail-450x450-white-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    const [state, setState] = React.useState({value: ""});
    const textarea = React.createRef();

    function handleChange(event: { target: { value: string } }) {
        setState({
            value: event.target.value,
        });
    }

    return (
        <Row>
            <Col>
                {pictures?.map(p => (
                    <Row key={p.id} className="mb-4">
                        <Col className="col-3">
                            <img src={thumbnail(p)} width="100%"/>
                        </Col>
                        <Col>
                            <h5>{p.name}</h5>
                            <div>
                                {editMode ?
                                    <div>
                                        <Form.Control as="textarea" rows={3}/>
                                        <MentionsInput
                                            value={state.value}
                                            onChange={handleChange}
                                            inputRef={textarea as React.Ref<HTMLTextAreaElement>}
                                        >
                                            <Mention
                                                trigger="#"
                                                data={[
                                                    {id: 'Lenna', display: '#Lenna'},
                                                    {id: 'Lena', display: '#Lena'},
                                                    {id: '1973', display: '#1973'},
                                                ]}
                                            />
                                        </MentionsInput>
                                    </div> :
                                    <Description tagRegions={p.tags}>
                                        {p.description}
                                    </Description>
                                }
                            </div>
                        </Col>
                    </Row>
                ))}
            </Col>
        </Row>
    );
}