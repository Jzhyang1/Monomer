export default function List({
  children,
  isNumber = false,
  className,
  ...props
}) {
  return (
    <div className={`pl-6 ${className}`} {...props}>
      <ul className={`list-outside ${isNumber ? "list-decimal" : "list-disc"}`}>
        {children.map((child) => (
          <li>{child}</li>
        ))}
      </ul>
    </div>
  );
}
