export type Tag = {
    start: number,
    end: number
};

export type Link = {
    rel: string,
    href: string
}

export type Picture = {
    id: number,
    name: string,
    description: string,
    tags: Tag[],
    links: Link[]
};

export type AvailableTag = {
    tag: string,
    count: number
}

export function picturesUrl() {
    return process.env.REACT_APP_GALLERY_API_ROOT + '/pictures/';
}