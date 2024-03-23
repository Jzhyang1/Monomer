export default function List({
  children,
  isNumber = false,
  className,
  ...props
}) {
  return (
    <div className={`pl-6 leading-7 ${className}`} {...props}>
      <ul className={`list-outside ${isNumber ? "list-decimal" : "list-disc"}`}>
        {children.map((child, i) => (
          <li key={i}>{child}</li>
        ))}
      </ul>
    </div>
  );
}
