export type PropsType = {
  title?: string;
  code?: string;
  link?: string;
  className?: string;
  color?: string;
  creds?: string;
  image?: string;
  desc?: string;
  href?: string;
  pages?: string[];
  links?: LinkType[];
  important?: boolean;
  samePage?: boolean;
  expand?: boolean;
  initial?: boolean;
  colored?: boolean;
  blocked?: boolean;
  contact?: ContactType;
  symbol?: React.ReactNode;
  header?: React.ReactNode;
  children?: React.ReactNode;
};

export type ContactType = {
  title?: string;
  creds?: string;
  link?: string;
  image?: string;
  desc?: string;
  links?: LinkType[];
  important?: boolean;
};

export type LinkType = {
  title?: string;
  link?: string;
};
